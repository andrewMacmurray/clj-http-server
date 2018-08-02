(defproject clj-http-server "0.1.0-SNAPSHOT"
  :description "8th light cob spec compliant http server"
  :url "https://github.com/andrewMacmurray/clj-http-server"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :main clj-http-server.core
  :aot [clj-http-server.core]
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :profiles {:dev {:dependencies [[speclj "3.3.2"]]}}
  :plugins [[speclj "3.3.2"]]
  :test-paths ["spec"])
