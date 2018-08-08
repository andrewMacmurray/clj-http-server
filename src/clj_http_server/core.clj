(ns clj-http-server.core
  (:gen-class)
  (:require [clj-http-server.server :as server]
            [clj-http-server.cob-routes :refer [cob-routes]]))

(def auth-config {:username "admin" :password "hunter2"})

(defn -main
  [& args]
  (do
    (println "starting server")
    (server/serve 5000 (cob-routes "public" auth-config))))
