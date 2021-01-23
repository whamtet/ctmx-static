(ns ctmx_static.ctmx
  (:require
    [ctmx.rt :as rt]))

(def parsers
  {:int `rt/parse-int
   :float `rt/parse-float
   :boolean `rt/parse-boolean
   :boolean-true `rt/parse-boolean-true})

(defmacro p []
  `rt/parse-int)
