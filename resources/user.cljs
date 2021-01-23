(ns cljs.user
  (:require-macros
    [ctmx.core :as ctmx]))

(ctmx/defcomponent ^:endpoint form [req first-name]
  [:form {:id id :hx-get "form"}
   [:input {:type "submit"}]])
