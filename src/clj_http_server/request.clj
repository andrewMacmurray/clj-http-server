(ns clj-http-server.request
  (:require [clojure.string :as str]))

(defn- request-line [reader]
  (str/split (.readLine reader) #" " 3))

(defn- parse-uri [full-uri]
  (str/split full-uri #"\?" 2))

(defn- parse-param [query-param]
  (let [[k v] (str/split query-param #"=" 2)]
    {k v}))

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

(defn parse-request [reader]
  (let [[method full-uri version] (request-line reader)
        [uri params] (parse-uri full-uri)]
    {:method method
     :version version
     :uri uri
     :params (parse-params? params)}))
