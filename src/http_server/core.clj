(ns http-server.core
  (:gen-class)
  (:require [http-server.server :as server]
            [http-server.cob-app :refer [app-handler]]))

(def auth-config {:username "admin" :password "hunter2"})

(defn -main
  [& args]
  (println "starting server")
  (server/serve 5000 (app-handler "public" auth-config)))
