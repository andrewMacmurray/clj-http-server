(ns clj-http-server.utils.logging
  (:require [clojure.string :as str]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.core :as appenders]))

(timbre/merge-config!
 {:appenders {:spit (appenders/spit-appender {:fname "public/logs"})}})

(defn log-request [{:keys [method uri version], :as request}]
  (timbre/info (str/join " " [method uri version])) request)

(defn log-response [{:keys [status], :as response}]
  (timbre/info (str/join " " ["STATUS" status])) response)
