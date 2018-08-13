(ns clj-http-server.request-spec
  (:require [speclj.core :refer :all]
            [clojure.java.io :as io]
            [clj-http-server.request :refer :all]))

(defn reader [request-lines]
  (io/reader (char-array request-lines)))

(defn run-parser [request-lines]
  (parse-request (reader request-lines)))

(describe "parse-request"
          (it "parses method correctly"
              (let [request (run-parser "GET / HTTP/1.1\r\n\r\n")
                    method (:method request)]
                (should= "GET" method)))

          (it "parses version correctly"
              (let [request (run-parser "POST / HTTP/1.1\r\n\r\n")
                    version (:version request)]
                (should= "HTTP/1.1" version)))

          (it "parses uri without params correctly"
              (let [request (run-parser "GET /hello HTTP/1.1\r\n\r\n")
                    uri (:uri request)]
                (should= "/hello" uri)))

          (it "parses uri with params correctly"
              (let [request (run-parser "GET /hello?foo=bar HTTP/1.1\r\n\r\n")
                    uri (:uri request)]
                (should= "/hello" uri)))

          (it "parses uri with encoded params correctly"
              (let [params "Operators%20%3C%2C"
                    expectedParams "Operators <,"
                    request (run-parser (str "GET /hello?foo=" params))]
                (should= {"foo" expectedParams} (:params request))))

          (it "parses params correctly"
              (let [request (run-parser "GET /hello?foo=bar&hello=world HTTP/1.1\r\n\r\n")
                    params (:params request)]
                (should= {"foo" "bar", "hello" "world"} params)))

          (it "parses headers correctly"
              (let [request (run-parser "GET / HTTP/1.1\r\nConnection: Keep-Alive\r\nContent-Type: text/html\r\n\r\n")
                    headers (:headers request)]
                (should= {"Content-Type" "text/html", "Connection" "Keep-Alive"} headers)))

          (it "parses body correctly"
              (let [request (run-parser "GET / HTTP/1.1\r\nContent-Length: 11\r\n\r\nhello world")
                    body (:body request)]
                (should= "hello world" body))))
