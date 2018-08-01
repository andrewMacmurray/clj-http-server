(ns clj-http-server.router)

(def not-found {:status 404
                :headers {}
                :body "not found"})

(def server-error {:status 500
                   :headers {}
                   :body "internal server error"})

(defn- find-handler [routes request]
  (let [uri (:uri request)]
    (get routes uri)))

(defn- run-request [handle request]
  (try
    (if (nil? handle) not-found (handle request))
    (catch Exception e server-error)))

(defn respond [routes request]
  (let [handle (find-handler routes request)]
    (run-request handle request)))
