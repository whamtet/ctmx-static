(def s1 "<script src=\"/out/ctmx_static.js\"></script>")
(def s2 "<script>loadCljs('%s')</script>")

(defn copy-index [from path]
  (spit (str "dist/" from)
        (-> from
            slurp
            (.replace s1 (format s2 path)))))

(copy-index "index.html" "ctmx_static.js")
(copy-index "examples/click-to-edit.htm" "../ctmx_static.js")

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
