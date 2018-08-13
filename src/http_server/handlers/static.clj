(ns http-server.handlers.static
  (:require [http-server.utils.file :refer :all]
            [http-server.utils.function :refer :all]
            [http-server.handlers.partial :refer :all]
            [clojure.string :as str]
            [http-server.routing.responses :refer :all]))

(defn- get-path [{:keys [static-dir uri]}]
  (str static-dir uri))

(defn- serve-file [path]
  {:status 200
   :body (read-file path)})

(defn- serve [request]
  (if (partial-request? request)
    (serve-partial request)
    (serve-file (get-path request))))

(defn get-static [request]
  (let [path (get-path request)]
    (if (is-file? path)
      (serve request) not-found)))

(defn put-static [request]
  (let [path (get-path request)
        file-exists (is-file? path)]
    (write-file path (:body request))
    (if file-exists
      {:status 200}
      {:status 201})))

(defn delete-static [request]
  (let [path (get-path request)]
    (delete-file path)
    {:status 200}))
