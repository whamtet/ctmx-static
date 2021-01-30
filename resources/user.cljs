(ns cljs.user
  (:require-macros
    [ctmx.core :refer [defcomponent]]))

(defn json [s]
  (-> s clj->js js/JSON.stringify))

(def make-routes ctmx-static.rt/send-root!)
