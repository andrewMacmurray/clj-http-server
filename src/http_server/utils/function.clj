(ns http-server.utils.function
  (:require [clojure.string :as str]))

(def not-nil? (complement nil?))

(defn not-empty?
  "returns true if value is not empty"
  [x]
  (not (empty? x)))

(defn in?
  "true if coll contains x"
  [coll x]
  (some #(= x %) coll))

(defn find-first
  "returns first matching element in a collection"
  [f coll]
  (first (filter f coll)))

(defn string-to-map
  "splits a string into a key value pair based on a delimiter"
  [delimiter string]
  (let [[k v] (str/split string delimiter 2)]
    {k v}))

(defn- combine-keys [delimiter]
  (fn [acc v] (merge (string-to-map delimiter v) acc)))

(defn split-map
  "creates a map from a collection of strings separated by a delimiter"
  [coll delimiter]
  (reduce (combine-keys delimiter) {} coll))

(defn update-map-values
  "applies a function to each value in a map"
  [coll f]
  (reduce-kv (fn [m k v] (assoc m k (f v))) {} coll))
