(ns http-server.server
  (:require [clojure.java.io :as io]
            [http-server.response :as response]
            [http-server.request  :as request])
  (:import [java.net ServerSocket]
           [java.util.concurrent Executors]))

(defn read-request [app-handler reader]
  (->> reader
       (request/parse-request)
       (app-handler)
       (response/build-response)))

(defn write-response [response writer]
  (.write writer response)
  (.flush writer))

(defn- exec-request [app-handler sock]
  (with-open [reader (io/reader sock)
              writer (io/output-stream sock)]
    (-> app-handler
        (read-request reader)
        (write-response writer))))

(defn serve [port app-handler]
  (with-open [server-sock (ServerSocket. port)
              thread-pool (Executors/newFixedThreadPool 20)]
    (loop []
      (let [sock    (.accept server-sock)
            request (partial exec-request app-handler)]
        (.execute thread-pool #(request sock)))
      (recur))))
