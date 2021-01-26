(ns ctmx-static.util
  (:require
    [clojure.java.io :as io]))

(defmacro slurpm [s]
  (-> s io/resource slurp))

(defmacro defwindow [name args & body]
  `(set!
     ~(symbol (str "js/window." name))
     (wrap-window (fn ~args ~@body))))
