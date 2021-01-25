(ns cljs.user
  (:require-macros
    [ctmx.core :as ctmx]))

(ctmx/defcomponent ^:endpoint my-endpoint [req])

(defn endpoints []
  (->> 'cljs.user
       ns-interns
       vals
       (map meta)
       (filter :endpoint)
       (map #(-> % :name str))
       set))
