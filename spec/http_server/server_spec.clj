(ns http-server.server-spec
  (:require [speclj.core :refer :all]
            [clojure.java.io :as io]
            [http-server.server :as server]
            [http-server.response :as response])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream]))

(def get-foo "GET /foo HTTP/1.1\r\n\r\n")
(def foo-response {:status 200 :headers {} :body "foo!"})
(defn foo-handler [_] foo-response)

(defn socket-in [in]
  (io/reader (ByteArrayInputStream. (.getBytes in))))

(defn socket-out []
  (io/writer (ByteArrayOutputStream.)))

(describe "server"
          (it "processes a request from socket in"
              (let [response (server/read-request foo-handler (socket-in get-foo))]
                (should= (String. (response/build-response foo-response)) (String. response))))

          (it "writes a response to socket out"
              (let [socket-out (ByteArrayOutputStream.)]
                (server/write-response (.getBytes "response\r\n") socket-out)
                (should= "response\r\n" (.toString socket-out)))))
