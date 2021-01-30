(ns ctmx-static.build
  (:require
    clojure.pprint
    [clojure.string :as string]
    [clojure.data.json :as json]
    [ctmx.core :refer [defcomponent]]
    [hiccup.core :as hiccup]))

(def split #(.split % "\n"))
(def rejoin #(string/join "\n" %))
(def pre? #(or (.contains % "<pre") (.contains % "</pre")))

(defn tidy-src [src]
  (-> src
      rejoin
      (.replace "cljs.pprint" "clojure.pprint")
      (.replace "cljs.user" "ctmx-static.build")
      (.replace ".includes" ".contains")))

(defn get-source [i]
  (->> i
       split
       (remove #(.contains % "(use 'ctmx.core)"))
       (partition-by pre?)
       (take-nth 2)
       rest
       (take-nth 2)
       (map tidy-src)
       (filter #(.contains % "make-routes"))))

(defn html [f]
  (-> nil f hiccup/html))

(def to-insert (atom {}))

(defn make-routes [route f]
  (swap! to-insert
         assoc (.replace route "/" "") (html f)))

(def json json/write-str)

(defn eval-all [s]
  (binding [*ns* (find-ns 'ctmx-static.build)]
    (->> s
         (format "(do %s)" s)
         read-string
         eval)))

(defn set-content [src]
  (doseq [src (get-source src)]
    (eval-all src)))

(defn replace-div [s [k v]]
  (.replace s
            (format "<div id=\"%s\">" k)
            (format "<div id=\"%s\">%s" k v)))

(defn populate-html [src]
  (set-content src)
  (reduce replace-div src @to-insert))
