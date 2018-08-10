(ns clj-http-server.routing.router
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clj-http-server.routing.responses :refer :all]
            [clj-http-server.routing.route :refer :all]
            [clj-http-server.handlers.static :refer :all]
            [clj-http-server.utils.file :refer :all]
            [clj-http-server.utils.function :refer :all])
  (:import [java.io ByteArrayOutputStream]
           [java.nio.file Paths Files]))

(def allowed-methods #{"GET" "OPTIONS" "PUT" "POST" "HEAD" "DELETE" "PATCH"})

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

(defn- bogus-request? [request]
  (let [method (:method request)]
    (not (in? allowed-methods method))))

(defn- options-fallback [uri routes]
  (let [options (find-first (partial has-options? uri) routes)]
    (if options not-allowed-handle)))

(defn- match-route [routes request]
  (let [uri          (:uri request)
        method       (:method request)
        route-match  (partial matching-route? uri method)
        static-match (partial static-request? method)
        not-allowed  (options-fallback uri routes)]
    (or
     (find-first route-match routes)
     (find-first static-match routes)
     not-allowed)))

(defn- run-request-with-fallbacks [route request]
  (let [bogus?    (bogus-request? request)
        no-match? (nil? route)
        handle    (:handler route)]
    (cond
      bogus?    method-not-allowed
      no-match? not-found
      :else     (handle request))))

(defn- run-request [route request]
  (try
    (run-request-with-fallbacks route request)
    (catch Exception e)))

(defn respond [routes request]
  (let [handle (match-route routes request)]
    (run-request handle request)))
