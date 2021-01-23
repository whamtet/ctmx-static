(ns ctmx-static.core
  (:require
    #?(:clj [clojure.java.io :as io])
    #?(:clj [ctmx.core :as ctmx])
    #?(:cljs [ctmx_static.eval])
    [ctmx.rt :as rt])
  #?(:cljs
      (:require-macros
        [ctmx.core :as ctmx])))

#?(:cljs
    (enable-console-print!))

(ctmx/defcomponent ^:endpoint form [req first-name]
  [:form {:id id :hx-get "form"}
   [:input {:type "text" :name (path "first-name") :value first-name}]
   [:input {:type "submit"}]])

#_(ctmx/defstatic page [release?]
  [:head
   [:title "ctmx-static"]
   [:meta {:http-equiv "Content-Type" :content "text/html; charset=utf-8"}]]
  [:body {:hx-ext "static"}
   (form nil "hello")]
  [:script {:src "/htmx.min.js"}]
  [:script {:src (if release?
                   "/ctmx_static.js"
                   "/out/ctmx_static.js")}]
  [:script "htmx.config.defaultSettleDelay = 0;
   htmx.config.defaultSwapStyle = 'outerHTML';"]
  [:script (-> "htmx_wrapper.js"
               io/resource
               slurp)])

(defn page [release?] "")

(defn routes [release?]
  {"" (page release?)
   "subroute" (page release?)})
