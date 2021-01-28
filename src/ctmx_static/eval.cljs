(ns ctmx_static.eval
  (:require
    [cljs.js]
    [clojure.string :as string]
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

(defn- filter-use [s]
  (->> (.split s "\n")
       (remove #(.includes % "(use "))
       (string/join "\n")))

(defn eval-endpoints [source]
  (binding [*print-err-fn*
            (if (-> source .trim (.startsWith ";s"))
              *print-err-fn*
              (constantly nil))]
    (-> source
        filter-use
        (str " " (util/slurpm "endpoints.cljs"))
        (eval-raw  #(-> % :value rt/update-endpoints)))))

(defn init []
  (eval-endpoints (util/slurpm "user.cljs"))
  (doseq [editor js/editors]
    (-> editor .getSession .getValue eval-endpoints))
  (println "loaded"))

(set! js/e #(eval-raw % prn))
