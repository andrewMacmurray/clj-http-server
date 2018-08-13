(ns clj-http-server.handlers.partial-spec
  (:require [speclj.core :refer :all]
            [clj-http-server.utils.file :refer :all]
            [clj-http-server.handlers.partial :refer :all]))

(describe "partial-request?"
          (it "returns true if the request is for partial content"
              (let [request {:uri "/file.txt" :headers {"Range" "bytes=0-4"}}
                    partial? (partial-request? request)]
                (should= true partial?)))

          (it "returns false if request is for full content"
              (let [request {:uri "/file.txt"}
                    partial? (partial-request? request)]
                (should= false partial?))))

(describe "serve-partial with full range"
          (with-stubs)
          (around [it]
                  (with-redefs [read-file    (constantly (.getBytes "some file content"))
                                read-partial (stub :read-partial-stub)]))

          (let [request {:uri "/file.txt"
                         :static-dir "public"
                         :headers {"Range" "bytes=0-4"}}]

            (it "responds with a 206 for successful response"
                (let [response (serve-partial request)]
                  (should= 206 (:status response))))

            (it "responds with correct content range header"
                (let [response (serve-partial request)
                      content-range (get-in response [:headers "Content-Range"])]
                  (should= "bytes 0-4/17" content-range)
                  (should-have-invoked :read-partial-stub {:with ["public/file.txt" 0 4]})))))

(describe "serve-partial with range start"
          (with-stubs)
          (around [it]
                  (with-redefs [read-file (constantly (.getBytes "some file content"))
                                read-partial (stub :read-partial-stub)]))

          (it "responds with correct content range header"
              (let [request {:uri "/file.txt"
                             :static-dir "public"
                             :headers {"Range" "bytes=4-"}}
                    response (serve-partial request)
                    content-range (get-in response [:headers "Content-Range"])]
                (should= "bytes 4-16/17" content-range)
                (should-have-invoked :read-partial-stub {:with ["public/file.txt" 4 16]}))))

(describe "serve-partial with range end"
          (with-stubs)
          (around [it]
                  (with-redefs [read-file (constantly (.getBytes "some file content"))
                                read-partial (stub :read-partial-stub)]))

          (it "responds with correct content range header"
              (let [request {:uri "/file.txt"
                             :static-dir "public"
                             :headers {"Range" "bytes=-8"}}
                    response (serve-partial request)
                    content-range (get-in response [:headers "Content-Range"])]
                (should= "bytes 8-16/17" content-range)
                (should-have-invoked :read-partial-stub {:with ["public/file.txt" 8 16]}))))
