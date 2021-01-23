(require '[ctmx.core :as ctmx])
(use 'ctmx-static.core)

(defn reload []
  #_ #_(use 'ctmx-static.core :reload)
  (ctmx/spit-static "./" (routes false)))

(require '[cljs.build.api :as b])

(b/watch "src"
         {:main 'ctmx-static.core
          :output-to "out/ctmx_static.js"
          :output-dir "out"
          :watch-fn reload})
