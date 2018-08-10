(ns clj-http-server.handlers.static
  (:require [clj-http-server.utils.file :refer :all]
            [clj-http-server.utils.function :refer :all]
            [clj-http-server.handlers.partial :refer :all]
            [clojure.string :as str]
            [clj-http-server.routing.response :refer :all]))

(def content-types {".txt"  "text/plain"
                    ".html" "text/html"
                    ".png"  "image/png"
                    ".gif"  "image/gif"
                    ".jpeg" "image/jpeg"})

(defn- get-path [{:keys [static-dir uri]}]
  (str static-dir uri))

(defn- content-type-header [path]
  (let [content-type (get content-types (file-ext path))]
    (if content-type {"Content-Type" content-type} {})))

(defn serve-file [path]
  {:status 200
   :headers (content-type-header path)
   :body (read-file path)})

(defn serve [request]
  (if (partial-request? request)
    (serve-partial request)
    (serve-file (get-path request))))

(defn get-static [request]
  (let [path (get-path request)]
    (if (is-file? path)
      (serve request) not-found)))

(defn put-static [request]
  (let [path (get-path request)
        file-exists (is-file? path)]
    (write-file path (:body request))
    (if file-exists
      {:status 200}
      {:status 201})))

(defn delete-static [request]
  (let [path (get-path request)]
    (delete-file path)
    {:status 200}))
