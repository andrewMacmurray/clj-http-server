(ns clj-http-server.server
  (:require [clojure.java.io :as io]
            [clj-http-server.response :as response]
            [clj-http-server.router   :as router]
            [clj-http-server.request  :as request])
  (:import [java.net ServerSocket]
           [java.io ByteArrayOutputStream]))

(defn run-request [routes reader]
  (->> reader
       (request/parse-request)
       (router/respond routes)
       (response/build-response)))

(defn write-response [response writer]
  (let [len (count response)]
    (.write writer response 0 len)
    (.flush writer)))

(defn serve [port routes]
  (with-open [server-sock (ServerSocket. port)]
    (loop []
      (with-open [sock (.accept server-sock)
                  reader (io/reader sock)
                  writer (io/writer sock)]
        (let [response (run-request routes reader)]
          (write-response response writer)))
      (recur))))
