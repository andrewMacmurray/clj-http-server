(ns clj-http-server.middleware.allowed-methods
  (:require [clj-http-server.utils.function :refer :all]
            [clj-http-server.routing.responses :refer [method-not-allowed]]))

(def allowed-methods #{"GET"
                       "OPTIONS"
                       "PUT"
                       "POST"
                       "HEAD"
                       "DELETE"
                       "PATCH"})

(defn- bogus-request? [request]
  (let [method (:method request)]
    (not (in? allowed-methods method))))

(defn with-allowed-methods [handler]
  (fn [request]
    (if (bogus-request? request)
      method-not-allowed (handler request))))
