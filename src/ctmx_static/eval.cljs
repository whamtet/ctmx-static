(ns ctmx_static.eval
  (:require
    [cljs.js])
  (:require-macros
    [ctmx-static.util :as util]))

(def state (cljs.js/empty-state))

(defn load-fn [args cb]
  (cb {:lang :clj
       :source
       (if (-> args :name (= 'ctmx.core))
         (util/slurpm "ctmx/core.cljs")
         "")}))

(defn eval [source]
  (cljs.js/eval-str state source nil {:eval cljs.js/js-eval
                                      :load load-fn} prn))

(defn init []
  (eval (util/slurpm "cljs/user.cljs")))

(set! js/window.onload init)
(set! js/e eval)
