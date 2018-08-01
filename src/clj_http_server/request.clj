(ns clj-http-server.request
  (:require [clojure.string :as str]))

(defn- not-empty? [x]
  (not (empty? x)))

(defn- request-line [reader]
  (str/split (.readLine reader) #" " 3))

(defn- header-lines [reader]
  (take-while not-empty? (repeatedly #(.readLine reader))))

(defn- read-body [reader length]
  (let [chrs (char-array length)]
    (.read reader chrs 0 length)
    (String. chrs)))

(defn- parse-uri [full-uri]
  (str/split full-uri #"\?" 2))

(defn- string-to-map [pattern string]
  (let [[k v] (str/split string pattern 2)]
    {k v}))

(defn- parse-param [query-param]
  (string-to-map #"=" query-param))

(defn- parse-header [header]
  (string-to-map #":\s*" header))

(defn- parse-params [query-string]
  (let [params (str/split query-string #"&")]
    (reduce
     (fn [acc v] (merge (parse-param v) acc))
     {}
     params)))

(defn- parse-params? [query-string]
  (if (empty? query-string)
    {}
    (parse-params query-string)))

(defn- parse-headers [reader]
  (let [raw-headers (header-lines reader)]
    (reduce
     (fn [acc v] (merge (parse-header v) acc))
     {}
     raw-headers)))

(defn- content-length [headers]
  (Integer/parseInt (get headers "Content-Length")))

(defn- parse-body [headers reader]
  (if (contains? headers "Content-Length")
    (read-body reader (content-length headers))
    nil))

(defn parse-request [reader]
  (let [[method full-uri version] (request-line reader)
        [uri params] (parse-uri full-uri)
        headers (parse-headers reader)
        body (parse-body headers reader)]
    {:method method
     :version version
     :uri uri
     :headers headers
     :params (parse-params? params)
     :body body}))
