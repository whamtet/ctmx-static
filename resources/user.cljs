(ns cljs.user
  (:require-macros
    [ctmx.core :as ctmx]))

(ctmx/defendpoint ^:endpoint my-endpoint [req])

#_(defn endpoints []
  (->> 'cljs.user
       ns-interns
       vals
       (map meta)
       (filter :endpoint)
       (map #(-> % :name str))
       set))
