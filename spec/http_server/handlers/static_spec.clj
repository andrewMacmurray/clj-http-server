(ns http-server.handlers.static-spec
  (:require [speclj.core :refer :all]
            [http-server.utils.file :refer :all]
            [http-server.handlers.partial :refer :all]
            [http-server.handlers.static :refer :all]))

(def put-file-request {:method "PUT" :body "some text" :uri "/file.txt" :static-dir "public"})

(describe "put static"
          (with-stubs)
          (it "saves a new file to the static directory"
              (with-redefs [write-file (fn [_ _] nil)
                            is-file? (constantly false)]
                (let [response (put-static put-file-request)]
                  (should= 201 (:status response)))))

          (it "responds with 200 if the file already exists"
              (with-redefs [write-file (fn [_ _] nil)
                            is-file? (constantly true)]
                (let [response (put-static put-file-request)]
                  (should= 200 (:status response)))))

          (it "saves the body of the request to a file"
              (with-redefs [write-file (stub :write-file-stub)
                            is-file? (stub :is-file-stub {:return false})]
                (do
                  (put-static put-file-request)
                  (should-have-invoked :is-file-stub {:with ["public/file.txt"]})
                  (should-have-invoked :write-file-stub {:with ["public/file.txt" "some text"]})))))

(describe "delete static"
          (with-stubs)
          (it "deletes a file at a given path"
              (with-redefs [delete-file (stub :delete-file-stub)]
                (let [request {:method "DELETE" :uri "/file.txt" :static-dir "public"}
                      response (delete-static request)]
                  (should= 200 (:status response))
                  (should-have-invoked :delete-file-stub {:with ["public/file.txt"]})))))

(describe "get static"
          (with-stubs)
          (it "gets a file from a given uri"
              (with-redefs [is-file? (stub :is-file-stub {:return true})
                            read-file (constantly "file data")]
                (let [request {:uri "/file.txt" :static-dir "public"}
                      response (get-static request)]
                  (should-have-invoked :is-file-stub {:with ["public/file.txt"]})
                  (should= 200 (:status response))
                  (should= "file data" (:body response)))))

          (it "responds with 404 if can't find file"
              (with-redefs [is-file? (constantly false)]
                (let [request {:uri "/unknown.txt" :static-dir "public"}
                      response (get-static request)]
                  (should= 404 (:status response)))))

          (it "responds with a partial file given request range headers"
              (with-redefs [is-file? (constantly true)
                            serve-partial (stub :serve-partial-stub)]
                (let [request {:uri "/file.txt" :static-dir "public" :headers {"Range" "bytes=0-4"}}
                      _ (get-static request)]
                  (should-have-invoked :serve-partial-stub {:with [request]})))))
