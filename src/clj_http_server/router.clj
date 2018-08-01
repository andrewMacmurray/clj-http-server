(ns clj-http-server.router)

(defn find-first [f xs]
  (first (filter f xs)))

(def not-found
  {:status 404 :headers {} :body "not found"})

(def server-error
  {:status 500 :headers {} :body "internal server error"})

(defn GET [uri handler]
  {:uri uri :method "GET" :handler handler})

(defn HEAD [uri handler]
  {:uri uri :method "HEAD" :handler handler})

(defn OPTIONS [uri handler]
  {:uri uri :method "OPTIONS" :handler handler})

(defn- matching-route? [uri method route]
  (and
   (= (:uri route) uri)
   (= (:method route) method)))

(defn- find-handler [routes request]
  (let [uri (:uri request)
        request-method (:method request)
        match (partial matching-route? uri request-method)]
    (find-first match routes)))

(defn- run-request [route request]
  (try
    (if (nil? route) not-found ((:handler route) request))
    (catch Exception e server-error)))

(defn respond [routes request]
  (let [handle (find-handler routes request)]
    (run-request handle request)))
