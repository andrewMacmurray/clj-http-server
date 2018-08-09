(ns clj-http-server.handlers.static
  (:require [clj-http-server.utils.file :refer :all]
            [clj-http-server.routing.response :refer :all]))

(def content-types {".txt"  "text/plain"
                    ".html" "text/html"
                    ".png"  "image/png"
                    ".gif"  "image/gif"
                    ".jpeg" "image/jpeg"})

(defn- content-type-header [path]
  (let [content-type (get content-types (file-ext path))]
    (if content-type {"Content-Type" content-type} {})))

(defn serve-file [path]
  {:status 200
   :headers (content-type-header path)
   :body (read-file path)})

(defn get-static [public-dir]
  (fn [request]
    (let [path (str public-dir (:uri request))]
      (if (is-file? path)
        (serve-file path) not-found))))

(defn put-static [{:keys [uri static-dir body]}]
  (let [path (str static-dir uri)
        file-exists (file-exists? path)]
    (write-file path body)
    (if file-exists
      {:status 200}
      {:status 201})))

(defn delete-static [{:keys [uri static-dir]}]
  (let [path (str static-dir uri)]
    (delete-file path)
    {:status 200}))
