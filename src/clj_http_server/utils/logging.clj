(ns clj-http-server.utils.logging
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.core :as appenders]))

(def log-file-name "public/logs")

(io/delete-file log-file-name :quiet)
(timbre/merge-config!
 {:appenders {:spit (appenders/spit-appender {:fname log-file-name})}})

(defn log-request [{:keys [method uri version], :as request}]
  (timbre/info (str/join " " [method uri version])) request)

(defn log-response [{:keys [status], :as response}]
  (timbre/info (str/join " " ["STATUS" status])) response)
