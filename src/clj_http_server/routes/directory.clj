(ns clj-http-server.routes.directory
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn- to-html-link
  "creates a html link for given path"
  [path]
  (format "<a href='/%s'>%s</a>" path path))

(defn- file-links
  "creates html links for each file on the path"
  [path]
  (->> (io/file path)
       (.list)
       (seq)
       (map to-html-link)
       (str/join "\n")))

(defn directory-links [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (file-links (:static-dir request))})
