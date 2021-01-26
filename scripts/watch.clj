(require '[cljs.build.api :as b])

(b/watch "src"
         {:main 'ctmx-static.core
          :output-to "out/ctmx_static.js"
          :output-dir "out"})
