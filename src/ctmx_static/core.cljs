(ns ctmx-static.core
  (:require
    ctmx-static.eval
    ctmx-static.rt
    ;; for macroexpand
    [clojure.walk :as walk]
    [ctmx.render :as render]
    [ctmx.rt :as rt]))
