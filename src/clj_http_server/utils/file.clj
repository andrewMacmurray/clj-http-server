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

(defn read-partial
  "reads portion of a file into bytes"
  [path start end]
  (let [buffer-size (inc (- end start))
        buffer (byte-array buffer-size)]
    (with-open [stream (io/input-stream path)]
      (.skip stream start)
      (.read stream buffer))
    buffer))

(defn write-file
  "writes contents to file"
  [path contents]
  (with-open [writer (io/writer path)]
    (.write writer contents)))

(defn delete-file
  "deletes file at path"
  [path]
  (io/delete-file path :quiet))
