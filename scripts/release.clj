(require '[ctmx.core :as ctmx])
(use 'ctmx-static.core)

(ctmx/spit-static "dist/" (routes true))

(require '[cljs.build.api :as b])

(println "Building ...")

(let [start (System/nanoTime)]
  (b/build "src"
    {:output-to "dist/ctmx_static.js"
     :output-dir "release"
     :optimizations :advanced
     :verbose true})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
