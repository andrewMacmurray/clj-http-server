(ns clj-http-server.core
  (:gen-class)
  (:require [clj-http-server.server :as server]
            [clj-http-server.cob-routes :refer [cob-routes]]))

(defn -main
  [& args]
  (do
    (println "starting server")
    (server/serve 5000 (cob-routes "cob_spec/public"))))
