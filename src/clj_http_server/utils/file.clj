(ns clj-http-server.utils.file
  (:require [clojure.java.io :as io]
            [clojure.string  :as str])
  (:import [java.nio.file Files]))

(defn file-ext
  "gets the file extension for a given path"
  [path]
  (let [dot-index  (str/last-index-of path ".")
        last-index (count path)]
    (if (nil? dot-index) "" (subs path dot-index last-index))))

(defn is-file?
  "checks if file exists at path and is not a directory"
  [path]
  (let [file (io/file path)]
    (and
     (.exists file)
     (not (.isDirectory file)))))

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
  (io/delete-file path :quiet))

(defn file-exists?
  "checks if file exists"
  [path]
  (.exists (io/file path)))
