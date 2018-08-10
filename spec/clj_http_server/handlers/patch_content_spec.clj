(ns clj-http-server.handlers.patch-content-spec
  (:require [speclj.core :refer :all]
            [clj-http-server.utils.file :refer :all]
            [clj-http-server.handlers.patch-content :refer :all]))

(describe "patch"
          (with-stubs)
          (it "responds with a 204 if patch successful"
              (with-redefs [write-file   (stub :write-file-stub)
                            read-file    (constantly (.getBytes "default content"))
                            is-file? (constantly true)]
                (let [request {:headers {"If-Match" "dc50a0d27dda2eee9f65644cd7e4c9cf11de8bec"}
                               :uri "/patch-content.txt"
                               :body "patched content"
                               :static-dir "public"}
                      response (patch-content request)]
                  (should-have-invoked :write-file-stub {:with ["public/patch-content.txt" "patched content"]})
                  (should= 204 (:status response))
                  (should= {"Content-Location" "/patch-content.txt"
                            "ETag" "dc50a0d27dda2eee9f65644cd7e4c9cf11de8bec"} (:headers response)))))

          (it "responds with 404 if file doesn't exist"
              (with-redefs [is-file? (constantly false)
                            read-file    (constantly nil)]
                (let [request {:headers {"If-Match" "dc50a0d27dda2eee9f65644cd7e4c9cf11de8bec"}
                               :uri "/unknown.txt"
                               :body "some content"
                               :static-dir "public"}
                      response (patch-content request)]
                  (should= 404 (:status response)))))

          (it "rejects patch request if hashes don't match"
              (with-redefs [is-file? (constantly true)
                            write-file   (fn [_ _] nil)
                            read-file    (constantly (.getBytes "this file doesn't match the hash"))]
                (let [request {:headers {"If-Match" "dc50a0d27dda2eee9f65644cd7e4c9cf11de8bec"}
                               :uri "/patch-content.txt"
                               :body "some content"
                               :static-dir "public"}
                      response (patch-content request)]
                  (should= 412 (:status response))))))
