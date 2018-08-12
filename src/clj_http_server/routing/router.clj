(ns clj-http-server.routing.router
  (:require [clj-http-server.routing.responses :refer :all]
            [clj-http-server.utils.function :refer :all]))

(defn- static-method? [method]
  (or
   (= method "GET")
   (= method "HEAD")))

(defn- static-request? [method route]
  (and
   (static-method? method)
   (:static-request route)))

(defn- matching-route? [uri method route]
  (and
   (= (:uri route) uri)
   (= (:method route) method)))

(defn- has-options? [uri route]
  (and
   (= (:uri route) uri)
   (= (:method route) "OPTIONS")))

(def not-allowed-handle
  {:handler (constantly method-not-allowed)})

(defn- options-not-allowed [uri routes]
  (let [options (find-first (partial has-options? uri) routes)]
    (if options not-allowed-handle)))

(defn- match-route [routes {:keys [uri method]}]
  (let [route-match  (partial matching-route? uri method)
        static-match (partial static-request? method)
        not-allowed  (options-not-allowed uri routes)]
    (or
     (find-first route-match routes)
     (find-first static-match routes)
     not-allowed)))

(defn- run-route [{handler :handler} request]
  (if handler (handler request) not-found))

(defn- try-request [route request]
  (try
    (run-route route request)
    (catch Exception e server-error)))

(defn respond [routes]
  (fn [request]
    (let [route (match-route routes request)]
      (try-request route request))))
