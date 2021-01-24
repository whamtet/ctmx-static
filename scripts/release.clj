(require '[ctmx.core :as ctmx])
(use 'ctmx-static.core)

;(ctmx/spit-static "dist/" (routes true))

(require '[cljs.build.api :as b])

(println "Building ...")

(let [start (System/nanoTime)]
  (b/build "src"
    {:output-to "dist/ctmx_static.js"
     :output-dir "release"
     :optimizations :simple
     :pretty-print false
     :optimize-constants true
     :static-fns true
     :verbose true})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
