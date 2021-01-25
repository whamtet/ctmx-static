(->> 'cljs.user
     ns-interns
     vals
     (map meta)
     (filter :endpoint)
     (map #(-> % :name str))
     set)
