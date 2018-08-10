(ns clj-http-server.handlers.patch-content
  (:require [clj-http-server.routing.response :refer :all]
            [clj-http-server.utils.sha1 :refer :all]
            [clj-http-server.utils.file :refer :all]))

(defn- get-if-match [request]
  (get-in request [:headers "If-Match"]))

(defn- patch-headers [request]
  (let [uri  (:uri request)
        etag (get-if-match request)]
    {"Content-Location" uri
     "ETag" etag}))

(defn- file-path [{:keys [static-dir uri]}]
  (str static-dir uri))

(defn- hashes-match? [request]
  (let [path         (file-path request)
        request-hash (get-if-match request)
        body-hash    (encode (read-file path))]
    (= request-hash body-hash)))

(defn- patch-file [request]
  (write-file (file-path request) (:body request)))

(defn- do-patch [request]
  (do
    (patch-file request)
    {:status 204
     :headers (patch-headers request)}))

(defn patch-content [request]
  (let [path (file-path request)]
    (cond
      (not (is-file? path)) not-found
      (not (hashes-match? request)) precondition-failed
      :else (do-patch request))))
