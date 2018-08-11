(ns clj-http-server.core
  (:gen-class)
  (:require [clj-http-server.server :as server]
            [clj-http-server.cob-app :refer [app-handler]]))

(def auth-config {:username "admin" :password "hunter2"})

(defn -main
  [& args]
  (do
    (println "starting server")
    (server/serve 5000 (app-handler "public" auth-config))))
