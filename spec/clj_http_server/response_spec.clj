(ns clj-http-server.response-spec
  (:require [speclj.core :refer :all]
            [clj-http-server.response :refer [build-response]]))

(describe "build-response"
          (it "builds a response with correct status line"
              (let [response (build-response {:status 200
                                              :headers {}
                                              :body ""})]
                (should= "HTTP/1.1 200 OK\r\n\r\n" response)))

          (it "adds correct heaers to the response"
              (let [response (build-response {:status 204
                                              :headers {"Content-Type" "text/html"}
                                              :body ""})]
                (should= "HTTP/1.1 204 No Content\r\nContent-Type: text/html\r\n\r\n" response)))

          (it "build response with correct headers and body if body is present"
              (let [response (build-response {:status 200
                                              :headers {}
                                              :body "hello world"})]
                (should= "HTTP/1.1 200 OK\r\nContent-Length: 11\r\n\r\nhello world" response))))
