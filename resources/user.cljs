(ns cljs.user
  (:require-macros
    [ctmx.core :as ctmx]))

(ctmx/defcomponent ^:endpoint hello [req ^:simple my-name]
  [:div#hello "Hello " my-name])

(ctmx/make-routes
  "/demo"
  (fn [req]
    [:div {:style "padding: 10px"}
     [:label {:style "margin-right: 10px"}
      "What is your name?"]
     [:input {:type "text"
              :name "my-name"
              :hx-patch "hello"
              :hx-target "#hello"
              :hx-swap "outerHTML"}]
     (hello req "")]))

(defn set-demo []
  (set! (.-outerHTML (js/document.getElementById "demo"))
    (ctmx-static.rt/wrap-response nil demo)))
