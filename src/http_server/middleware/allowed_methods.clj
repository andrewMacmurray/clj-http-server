(ns http-server.middleware.allowed-methods
  (:require [http-server.utils.function :refer :all]
            [http-server.routing.responses :refer [method-not-allowed]]))

(def allowed-methods #{"GET"
                       "OPTIONS"
                       "PUT"
                       "POST"
                       "HEAD"
                       "DELETE"
                       "PATCH"})

(defn- bogus-request? [{method :method}]
  (not (in? allowed-methods method)))

(defn with-allowed-methods [handler]
  (fn [request]
    (if (bogus-request? request)
      method-not-allowed (handler request))))
