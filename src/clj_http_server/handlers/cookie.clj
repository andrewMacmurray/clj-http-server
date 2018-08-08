(ns clj-http-server.handlers.cookie)

(defn- cookie-header [cookie-value]
  {"Set-Cookie" (str "type=" cookie-value)})

(defn- build-cookie-header [request]
  (let [cookie-val (get-in request [:params "type"])]
    (if cookie-val (cookie-header cookie-val) {})))

(defn cookie [request]
  {:status 200
   :headers (build-cookie-header request)
   :body "Eat"})
