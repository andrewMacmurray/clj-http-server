(ns clj-http-server.response
  (:require [clj-http-server.reasons :refer [reasons]]
            [clojure.string :as str]))

(def clrf "\r\n")

(defn- status-line [status]
  (str/join " " ["HTTP/1.1"
                 status
                 (get reasons status)]))

(defn- add-body-header [headers body]
  (if (empty? body)
    headers
    (assoc headers "Content-Length" (count body))))

(defn- build-header [[k v]]
  (str k ": " v))

(defn- append-clrf [string]
  (str string clrf))

(defn- build-headers [headers body]
  (let [extended-headers (add-body-header headers body)]
    (if (seq extended-headers)
      (->> extended-headers
           seq
           (map build-header)
           (str/join clrf)
           append-clrf))))

(defn build-response [{:keys [status headers body]}]
  (str (status-line status)
       clrf
       (build-headers headers body)
       clrf
       body))
