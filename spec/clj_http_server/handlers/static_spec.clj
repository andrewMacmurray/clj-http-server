(ns clj-http-server.handlers.static-spec
  (:require [speclj.core :refer :all]
            [clj-http-server.utils.file :refer :all]
            [clj-http-server.handlers.static :refer :all]))

(def put-file-request {:method "PUT" :body "some text" :uri "/file.txt" :static-dir "public"})

(describe "put static"
          (with-stubs)
          (it "saves a new file to the static directory"
              (with-redefs [write-file (fn [_ _] nil)
                            file-exists? (constantly false)]
                (let [response (put-static put-file-request)]
                  (should= 201 (:status response)))))

          (it "responds with 200 if the file already exists"
              (with-redefs [write-file (fn [_ _] nil)
                            file-exists? (constantly true)]
                (let [response (put-static put-file-request)]
                  (should= 200 (:status response)))))

          (it "saves the body of the request to a file"
              (with-redefs [write-file (stub :write-file-stub)
                            file-exists? (stub :file-exists-stub {:return false})]
                (do
                  (put-static put-file-request)
                  (should-have-invoked :file-exists-stub {:with ["public/file.txt"]})
                  (should-have-invoked :write-file-stub {:with ["public/file.txt" "some text"]})))))

(def delete-file-request {:method "DELETE" :uri "/file.txt" :static-dir "public"})

(describe "delete file"
          (with-stubs)
          (it "deletes a file at a given path"
              (with-redefs [delete-file (stub :delete-file-stub)]
                (let [response (delete-static delete-file-request)]
                  (should= 200 (:status response))
                  (should-have-invoked :delete-file-stub {:with ["public/file.txt"]})))))
