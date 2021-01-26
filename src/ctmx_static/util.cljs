(ns ctmx-static.util
  (:require
    [clojure.string :as string])
  (:require-macros
    [ctmx-static.util :as util]))

(defn wrap-window [f]
  (fn [& args]
    (->> args js->clj (apply f))))

(util/defwindow queryString [args]
  (if (-> args count pos?)
    (->> args
         (map (fn [[k v]] (str k "=" v)))
         (string/join "&")
         (str "?"))
    ""))
