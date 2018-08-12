(ns clj-http-server.middleware.content-type
  (:require [clj-http-server.utils.file :refer :all]))

(def content-types {".txt"  "text/plain"
                    ".html" "text/html"
                    ".png"  "image/png"
                    ".gif"  "image/gif"
                    ".jpeg" "image/jpeg"})

(defn- get-content-type [request]
  (->> (:uri request)
       (file-ext)
       (get content-types)))

(defn- add-header [response content-type]
  (assoc-in response [:headers "Content-Type"] content-type))

(defn- add-content-type [request response]
  (let [content-type (get-content-type request)]
    (if content-type (add-header response content-type) response)))

(defn with-content-type [handler]
  (fn [request]
    (let [response (handler request)]
      (add-content-type request response))))
