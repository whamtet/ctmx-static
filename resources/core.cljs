(ns ctmx.core
  (:require
    [clojure.walk :as walk]
    [ctmx.render :as render]
    [ctmx.rt :as rt]))

(def parsers
  {:int `rt/parse-int
   :float `rt/parse-float
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

(defn- expand-params [arg]
  (let [symbol (get-symbol arg)]
    (if (-> arg meta :simple)
      `(~(keyword symbol) ~'params)
      `(rt/get-value ~'params ~'stack ~(str symbol)))))

(defn- make-f [n args expanded]
  (case (count args)
    0 (throw (js/Error. "zero args not supported"))
    1 `(fn ~args ~expanded)
    `(fn this#
       ([req#]
         (let [{:keys [~'params]} req#
               ~'stack (rt/conj-stack ~(name n) req#)]
           (this#
             req#
             ~@(map expand-params (rest args)))))
       (~args
         (let [~@(for [sym (rest args)
                       :let [f (sym->f sym)]
                       :when f
                       x [sym `(~f ~sym)]]
                   x)]
           ~expanded)))))

(defn- with-stack [n [req] body]
  `(let [~'top-level? (empty? rt/*stack*)]
     (binding [rt/*stack* (rt/conj-stack ~(name n) ~(get-symbol req))
               rt/*params* (rt/get-params ~(get-symbol req))]
       (let [~'id (rt/path ".")
             ~'path rt/path
             ~'hash rt/path-hash
             ~'value (fn [p#] (-> p# rt/path keyword rt/*params*))]
         ~@body))))

(defn expand-parser-hint [x]
  (if-let [parser (sym->f x)]
    `(~parser ~(dissoc-parsers x))
    x))
(defn expand-parser-hints [x]
  (walk/prewalk expand-parser-hint x))

(defmacro update-params [f & body]
  `(binding [rt/*params* (~f rt/*params*)] ~@body))

(defmacro forall [& args]
  `(doall (for ~@args)))

(defmacro defcomponent [name args & body]
  `(def ~name
     ~(->> body
           expand-parser-hints
           (with-stack name args)
           (make-f name args))))
