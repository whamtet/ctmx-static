(ns cljs.user
  (:require-macros
    [ctmx.core :refer [defcomponent
                       make-routes]]))

(defn json [s]
  (-> s clj->js js/JSON.stringify))
