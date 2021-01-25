(spit "dist/index.html"
      (.replace
        (slurp "index.html")
        "out/ctmx"
        "ctmx"))

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
