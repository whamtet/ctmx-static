(ns ctmx_static.eval
  (:require
    [cljs.js]
    [ctmx_static.rt :as rt])
  (:require-macros
    [ctmx-static.util :as util]))

(def state (cljs.js/empty-state))

(defn load-fn [args cb]
  (cb {:lang :clj
       :source
       (if (-> args :name (= 'ctmx.core))
         (util/slurpm "core.cljs")
         "")}))

(defn eval-raw [source cb]
  (cljs.js/eval-str state source nil {:eval cljs.js/js-eval
                                      :load load-fn} cb))

(defn eval-endpoints [source]
  (eval-raw
    (str source
         " (->> 'cljs.user
         ns-interns
         vals
         (map meta)
         (filter :endpoint)
         (map #(-> % :name str))
         set)")
    #(-> % :value rt/update-endpoints)))

(defn eval [source] (eval-raw source prn))

(defn init []
  (binding [*print-err-fn* (constantly nil)]
    (eval-endpoints (util/slurpm "user.cljs")))
  (println "loaded"))

(set! js/window.onload init)
(set! js/e eval)
(def a 1)
