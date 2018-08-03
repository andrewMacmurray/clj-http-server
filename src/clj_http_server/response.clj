(ns clj-http-server.response
  (:require [clj-http-server.constants :refer [response-reasons clrf]]
            [clojure.string :as str]
            [clj-http-server.utils :refer :all])
  (:import [java.io ByteArrayOutputStream]))

(defn- status-line [status]
  (str/join " " ["HTTP/1.1"
                 status
                 (get response-reasons status)]))

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
           append-clrf) "")))

(defn- write-str [out x]
  (.write out (.getBytes x) 0 (count x)))

(defn- write-bytes [out b]
  (.write out b 0 (alength b)))

(defn- write [out x]
  (if (bytes? x)
    (write-bytes out x)
    (write-str   out x)))

(defn- body-bytes [body]
  (if (bytes? body) body (.getBytes body)))

(defn build-response [{:keys [status headers body]}]
  (let [out (ByteArrayOutputStream.)
        write (partial write out)]
    (write (status-line status))
    (write clrf)
    (write (build-headers headers body))
    (write clrf)
    (write body)
    (.toByteArray out)))
