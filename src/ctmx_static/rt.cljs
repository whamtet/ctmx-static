(ns ctmx-static.rt
  (:require
    [clojure.walk :as walk]
    hiccups.runtime)
  (:require-macros
    [hiccups.core :as hiccups]))

(def ^:private endpoints (atom #{}))

(defn update-endpoints [u]
  (reset! endpoints u))
(defn endpoint [s]
  (contains? @endpoints s))

(defn- lowercaseize [headers]
  (zipmap
    (map #(-> % name .toLowerCase) (keys headers))
    (vals headers)))

(defn coerce-static [{:keys [headers
                             parameters
                             verb]}]
  {:headers (lowercaseize headers)
   :params parameters
   :request-method (keyword verb)})

(defn wrap-response [req f]
  (some->> (or req {})
           js->clj
           walk/keywordize-keys
           coerce-static
           f
           hiccups/html))

(set! js/t #(prn @endpoints))
