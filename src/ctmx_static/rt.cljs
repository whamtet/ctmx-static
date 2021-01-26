(ns ctmx-static.rt
  (:require
    [clojure.walk :as walk]
    hiccups.runtime)
  (:require-macros
    [hiccups.core :as hiccups]))

(def ^:private endpoints (atom #{}))

(defn update-endpoints [u]
  (when u
    (reset! endpoints u)))
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

(def init-request
  {:headers {}
   :params {}
   :request-method :get})

(defn send-root! [path f]
  (let [id (.replace path "/" "")]
    (-> id js/document.getElementById .-innerHTML (set! (-> init-request f hiccups/html)))
    (-> id js/document.getElementById js/htmx.process)))

(set! js/t #(prn @endpoints))
