(ns clj-http-server.middleware)

(defn with-static [dir handler]
  (fn [request]
    (let [new-request (assoc request :static-dir dir)]
      (handler new-request))))
