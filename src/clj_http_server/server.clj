(ns clj-http-server.server
  (:require [clojure.java.io :as io]
            [clj-http-server.response :as response]
            [clj-http-server.router   :as router]
            [clj-http-server.request  :as request])
  (:import [java.net ServerSocket]
           [java.util.concurrent Executors]))

(defn read-request [routes reader]
  (->> reader
       (request/parse-request)
       (router/respond routes)
       (response/build-response)))

(defn write-response [response writer]
  (.write writer response)
  (.flush writer))

(defn- exec-request [routes sock]
  (with-open [reader (io/reader sock)
              writer (io/output-stream sock)]
    (-> routes
        (read-request reader)
        (write-response writer))))

(defn serve [port routes]
  (with-open [server-sock (ServerSocket. port)
              thread-pool (Executors/newFixedThreadPool 20)]
    (loop []
      (let [sock    (.accept server-sock)
            request (partial exec-request routes)]
        (.execute thread-pool #(request sock)))
      (recur))))
