(ns clj-http-server.request
  (:require [clojure.string :as str]
            [clj-http-server.utils.function :refer :all])
  (:import [java.net URLDecoder]))

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

(defn- decode-param [param]
  (-> param
      (str/trim)
      (URLDecoder/decode "UTF-8")))

(defn- parse-params [query-string]
  (-> query-string
      (str/split #"&")
      (split-map #"=")
      (update-map-values decode-param)))

(defn- parse-params? [query-string]
  (if (empty? query-string) {} (parse-params query-string)))

(defn- parse-headers [reader]
  (-> reader
      (header-lines)
      (split-map #":\s*")))

(defn- content-length [headers]
  (Integer/parseInt (get headers "Content-Length")))

(defn- parse-body [headers reader]
  (if (contains? headers "Content-Length")
    (read-body reader (content-length headers))))

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
