(ns clj-http-server.utils
  (:require [clojure.string :as str]))

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
