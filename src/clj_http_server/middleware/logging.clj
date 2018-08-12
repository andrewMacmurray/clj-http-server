(ns clj-http-server.middleware.logging
  (:require [clj-http-server.utils.logging :refer :all]))

(defn- home? [{uri :uri}]
  (= "/" uri))

(defn- filtered-log [request]
  (when-not (home? request) (log-request request)))

(defn with-logs [handler]
  (fn [request]
    (do
      (filtered-log request)
      (handler request))))
