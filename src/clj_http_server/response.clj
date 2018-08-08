(ns clj-http-server.response
  (:require [clojure.string :as str]
            [clj-http-server.utils.byte :refer :all])
  (:import [java.io ByteArrayOutputStream]))

(def clrf "\r\n")

(def response-reasons {200 "OK"
                       204 "No Content"
                       401 "Unauthorized"
                       404 "Not Found"
                       405 "Method Not Allowed"})

(defn- status-line [status]
  (str/join " " ["HTTP/1.1"
                 status
                 (get response-reasons status)]))

(defn- add-body-header [headers body]
  (if (empty? body)
    headers
    (assoc headers "Content-Length" (length body))))

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

(defn build-response [{:keys [status headers body]}]
  (let [out    (ByteArrayOutputStream.)
        append (fn [x] (write x out))]
    (append (status-line status))
    (append clrf)
    (append (build-headers headers body))
    (append clrf)
    (append body)
    (.toByteArray out)))
