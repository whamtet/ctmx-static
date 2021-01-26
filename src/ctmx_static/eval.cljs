(ns ctmx_static.eval
  (:require
    [cljs.js]
    [ctmx_static.rt :as rt])
  (:require-macros
    [ctmx-static.util :as util]))

(def state (cljs.js/empty-state))

(defn load-fn [{:keys [name]} cb]
  (cb {:lang :clj
       :source
       (if (= 'ctmx.core name)
         (util/slurpm "core.cljs")
         "")}))

(defn eval-raw [source cb]
  (cljs.js/eval-str state source nil {:eval cljs.js/js-eval
                                      :load load-fn} cb))

(defn eval-endpoints [source]
  (binding [*print-err-fn* (constantly nil)]
    (eval-raw
      (str source " " (util/slurpm "endpoints.cljs"))
      #(-> % :value rt/update-endpoints))))

(defn init []
  (eval-endpoints (util/slurpm "user.cljs"))
  (-> js/editor .getSession .getValue eval-endpoints)
  (println "loaded"))

(set! js/window.onload init)
(set! js/e #(eval-raw % prn))
