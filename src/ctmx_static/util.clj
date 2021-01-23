(ns ctmx-static.util
  (:require
    [clojure.java.io :as io]))

(defmacro slurpm [s]
  (-> s io/resource slurp))
