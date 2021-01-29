(ns ctmx.core
  (:require
    [clojure.walk :as walk]
    [ctmx.form :as form]
    [ctmx.render :as render]
    [ctmx.rt :as rt]))

(def parsers
  {:int `rt/parse-int
   :float `rt/parse-float
   :ints `rt/parse-ints
   :floats `rt/parse-floats
   :array `rt/parse-array
   :boolean `rt/parse-boolean
   :boolean-true `rt/parse-boolean-true})

(defn sym->f [sym]
  (when-let [meta (meta sym)]
    (some (fn [[k f]]
            (when (meta k)
              f))
          parsers)))

(defn dissoc-parsers [m]
  (apply vary-meta m dissoc (keys parsers)))

(defn- get-symbol [s]
  (if (symbol? s)
    s
    (do
      (-> s :as symbol? assert)
      (:as s))))
(defn- get-symbol-safe [s]
  (if (symbol? s)
    s
    (:as s)))
(defn- assoc-as [m]
  (if (and (map? m) (-> m :as not))
    (assoc m :as (gensym))
    m))

(def ^:private json? #(-> % meta :json))
(def ^:private annotations #{:simple :json :path})
(defn- some-annotation [arg]
  (->> arg meta keys (some annotations)))

(defn- expand-params [arg]
  (when-let [symbol (get-symbol-safe arg)]
    `(rt/get-value
       ~'params
       ~'json
       ~'stack
       ~(str symbol)
       ~(some-annotation arg))))

(defn- make-f [n args expanded]
  (when (empty? args)
    (throw (js/Error. "zero args not supported")))
  (let [pre-f (-> n meta :params)
        r (-> 0 args get-symbol)]
    (case (count args)
      1
      (if pre-f
        `(fn ~args
           (let [~r (update ~r :params form/apply-params ~pre-f ~r)] ~expanded))
        `(fn ~args ~expanded))
      `(fn this#
         ([~'req]
          (let [req# ~(if pre-f `(update ~'req :params form/apply-params ~pre-f ~'req) 'req)
                {:keys [~'params ~'stack]} (rt/conj-stack ~(name n) req#)
                ~'json ~(when (some json? args) `(form/json-params ~'params ~'stack))]
            (this#
              req#
              ~@(map expand-params (rest args)))))
         (~args
           (let [~@(for [sym (rest args)
                         :let [f (sym->f sym)]
                         :when f
                         x [sym `(~f ~sym)]]
                     x)]
             ~expanded))))))

(defn- with-stack [n [req] body]
  (let [req (get-symbol req)]
    `(let [~'top-level? (-> ~req :stack empty?)
           {:keys [~'params ~'stack] :as ~req} (rt/conj-stack ~(name n) ~req)
           ~'id (rt/path ~'stack ".")
           ~'path (partial rt/path ~'stack)
           ~'hash (partial rt/path-hash ~'stack)
           ~'value (fn [p#] (-> p# ~'path keyword ~'params))]
       ~@body)))

(defn expand-parser-hint [x]
  (if-let [parser (sym->f x)]
    `(~parser ~(dissoc-parsers x))
    x))
(defn expand-parser-hints [x]
  (walk/prewalk expand-parser-hint x))

(defmacro defcomponent [name args & body]
  (let [args (if (not-empty args)
               (update args 0 assoc-as)
               args)]
    `(def ~name
       ~(->> body
             expand-parser-hints
             (with-stack name args)
             (make-f name args)))))

(defmacro make-routes [path f]
  `(ctmx-static.rt/send-root! ~path ~f))
