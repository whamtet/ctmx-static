(ns ctmx-static.core
  (:require
    ctmx-static.eval
    ctmx-static.rt
    ctmx-static.util
    cljs.pprint
    ;; for macroexpand
    [clojure.walk :as walk]
    ctmx.form
    [ctmx.render :as render]
    [ctmx.rt :as rt]))
