(ns clj-http-server.utils.encoding
  (:import [java.util Base64]))

(defn encode
  "encodes a string to base64"
  [to-encode]
  (.encodeToString (Base64/getEncoder) (.getBytes to-encode)))

(defn decode
  "decodes base64 to string"
  [to-decode]
  (String. (.decode (Base64/getDecoder) to-decode)))
