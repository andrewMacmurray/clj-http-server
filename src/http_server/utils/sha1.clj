(ns http-server.utils.sha1
  (:import [java.security MessageDigest]))

(defn- get-hash-str [data]
  (apply str
         (map
          #(.substring (Integer/toString (+ (bit-and % 0xff) 0x100) 16) 1)
          data)))

(defn encode [data]
  (->> data
       (.digest (MessageDigest/getInstance "sha1"))
       (get-hash-str)))
