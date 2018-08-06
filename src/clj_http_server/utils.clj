(ns clj-http-server.utils
  (:require [clojure.string :as str]
            [clojure.java.io :as io])
  (:import [java.nio.file Files]))

;; general

(def not-nil? (complement nil?))

(defn not-empty?
  "returns true if value is not empty"
  [x]
  (not (empty? x)))

(defn in?
  "true if xs contains x"
  [xs x]
  (some #(= x %) xs))

(defn find-first
  "returns first matching element in a collection"
  [f xs]
  (first (filter f xs)))

(defn string-to-map
  "splits a string into a key value pair based on a regex pattern"
  [pattern string]
  (let [[k v] (str/split string pattern 2)]
    {k v}))

;; files

(defn file-ext
  "gets the file extension for a given path"
  [path]
  (let [dot-index (str/last-index-of path ".")
        last-index (.length path)]
    (if (nil? dot-index) "" (subs path dot-index last-index))))

(defn bytes?
  "checks if value is byte array"
  [x]
  (if (nil? x)
    false
    (= (Class/forName "[B")
       (.getClass x))))

(defn read-file
  "reads a file into bytes"
  [path]
  (->> (io/file path)
       (.toPath)
       (Files/readAllBytes)))

(defn write-file
  "writes contents to file"
  [path contents]
  (with-open [writer (io/writer path)]
    (.write writer contents)))

(defn delete-file
  "deletes file at path"
  [path]
  (io/delete-file path true))

(defn file-exists?
  "checks if file exists"
  [path]
  (.exists (io/file path)))

(defn to-html-link
  "creates a html link for given path"
  [path]
  (format "<a href='/%s'>%s</a>" path path))

(defn file-links
  "creates html links for each file on the path"
  [path]
  (->> (io/file path)
       (.list)
       (seq)
       (map to-html-link)
       (str/join "\n")))
