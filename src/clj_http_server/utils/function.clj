(ns clj-http-server.utils.function
  (:require [clojure.string :as str]))

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
  "splits a string into a key value pair based on a delimiter"
  [delimiter string]
  (let [[k v] (str/split string delimiter 2)]
    {k v}))

(defn- combine-keys [delimiter]
  (fn [acc v] (merge (string-to-map delimiter v) acc)))

(defn split-map
  "creates a map from a collection of strings separated by a delimiter"
  [xs delimiter]
  (reduce (combine-keys delimiter) {} xs))
