(ns clj-http-server.middleware.static)

(defn with-static [dir handler]
  (fn [request]
    (let [new-request (assoc request :static-dir dir)]
      (handler new-request))))
