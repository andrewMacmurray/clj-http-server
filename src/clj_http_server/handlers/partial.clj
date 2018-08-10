(ns clj-http-server.handlers.partial
  (:require [clj-http-server.utils.range :refer :all]
            [clj-http-server.utils.file :refer :all]
            [clj-http-server.utils.function :refer :all]))

(defn- get-path [{:keys [static-dir uri]}]
  (str static-dir uri))

(defn- get-range-header [request]
  (get-in request [:headers "Range"]))

(defn- range-string [from to file-length]
  (str "bytes " from "-" to "/" file-length))

(defn- invalid-range-string [file-length]
  (str "bytes */" file-length))

(defn partial-request? [request]
  (not-nil? (get-range-header request)))

(defn serve-partial [request]
  (let [path         (get-path request)
        file-length  (alength (read-file path))
        range-header (get-in request [:headers "Range"])
        [from to]    (calculate-range range-header file-length)
        valid        (valid-range? from to file-length)]

    (if valid
      {:status 206
       :headers {"Content-Range" (range-string from to file-length)
                 "Content-Type"  (file-ext path)}
       :body (read-partial path from to)}
      {:status 416
       :headers {"Content-Range" (invalid-range-string file-length)}})))
