(ns ctmx_static.eval
  (:require
    [cljs.js])
  (:require-macros
    [ctmx-static.util :as util]))

(def state (cljs.js/empty-state))

(defn load-fn [args cb]
  (cb {:lang :clj
       :source
       (if (-> args :name (= 'ctmx-static.ctmx))
         (util/slurpm "ctmx_static/ctmx.cljs")
         "")}))

(defn eval [source]
  (cljs.js/eval-str state source nil {:eval cljs.js/js-eval
                                      :load load-fn} prn))

(defn init []
  (eval "(ns cljs.user (:require-macros [ctmx-static.ctmx :as ctmx]))"))

(set! js/e eval)
