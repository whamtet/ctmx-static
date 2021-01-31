(require '[ctmx-static.build :as build])

(def s1 "<script src=\"/out/ctmx_static.js\"></script>\n<script>onload = () => ctmx_static.eval.init(true);</script>")
(def s2 "<script>setSrc('%s')</script>")

(defn copy-index [from path]
  (spit (str "dist/" from)
        (-> from
            slurp
            build/populate-html
            (.replace s1 (format s2 path)))))

(copy-index "index.html" "ctmx_static.js")
(doseq [f ["click-to-edit.htm" "bulk-update.htm" "click-to-load.htm" "delete-row.htm"
           "inline-validation.htm" "infinite-scroll.htm" "active-search.htm"
           "progress-bar.htm" "value-select.htm" "dialogs.htm" "modal-bootstrap.htm"
           "tabs-hateoas.htm" "sortable.htm"]]
  (copy-index (str "examples/" f) "../ctmx_static.js"))

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
