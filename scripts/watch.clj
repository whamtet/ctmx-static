(use 'ctmx-static.core)

(defn reload []
  (use 'ctmx-static.core :reload)
  (let [{:keys [text endpoints]} (page true)]
    (prn 'endpoints endpoints)
    (spit "index.html" text)
    (doseq [endpoint endpoints
            :let [[ns-name name] (.split endpoint "/")]]
      (spit
        name
        (-> endpoint (.replace "/" "."))))))

(require '[cljs.build.api :as b])

(b/watch "src"
         {:main 'ctmx-static.core
          :output-to "out/ctmx_static.js"
          :output-dir "out"
          :watch-fn reload})
