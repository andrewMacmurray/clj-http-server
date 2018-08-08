(ns clj-http-server.handlers.static
  (:require [clj-http-server.utils.file :refer :all]
            [clj-http-server.router :refer :all]))

(defn get-static [request]
  (let [handler (static-handler (:static-dir request))]
    (handler request)))

(defn put-static [{:keys [uri static-dir body]}]
  (let [path (str static-dir uri)
        file-exists (file-exists? path)]
    (write-file path body)
    (if file-exists
      {:status 200}
      {:status 201})))

(defn delete-static [{:keys [uri static-dir]}]
  (let [path (str static-dir uri)]
    (delete-file path)
    {:status 200}))
