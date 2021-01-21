(use 'ctmx-static.core)
(.mkdir (java.io.File. "dist"))
(let [{:keys [text endpoints]} (page true)]
  (prn 'endpoints endpoints)
  (spit "dist/index.html" text)
  (doseq [endpoint endpoints
          :let [[ns-name name] (.split endpoint "/")]]
    (spit
      (str "dist/" name)
      (-> endpoint (.replace "/" ".") (str "_static")))))

;(require '[cljs.build.api :as b])

(println "Building ...")

#_(let [start (System/nanoTime)]
  (b/build "src"
    {:output-to "dist/ctmx_static.js"
     :output-dir "release"
     :optimizations :advanced
     :verbose true})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
