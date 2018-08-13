(ns http-server.utils.byte)

(defprotocol Bytes
  (write  [x out])
  (length [x]))

(extend-protocol Bytes
  (Class/forName "[B")
  (write  [x out] (.write out x 0 (alength x)))
  (length [x] (alength x))

  nil
  (write  [x out] nil)
  (length [x] 0)

  java.lang.String
  (write  [x out] (write (.getBytes x) out))
  (length [x] (length (.getBytes x))))
