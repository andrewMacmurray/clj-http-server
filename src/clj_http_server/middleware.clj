(ns clj-http-server.middleware)

(defn with-static-dir [dir handler]
  (fn [request]
    (let [modified-request (assoc request :static-dir dir)]
      (handler modified-request))))
