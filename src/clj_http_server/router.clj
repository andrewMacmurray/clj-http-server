(ns clj-http-server.router
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clj-http-server.utils.file :refer :all]
            [clj-http-server.utils.function :refer :all])
  (:import [java.io ByteArrayOutputStream]
           [java.nio.file Paths Files]))

(def content-types {".txt"  "text/plain"
                    ".html" "text/html"
                    ".png"  "image/png"
                    ".gif"  "image/gif"
                    ".jpeg" "image/jpeg"})

(def allowed-methods #{"GET"
                       "OPTIONS"
                       "PUT"
                       "POST"
                       "HEAD"
                       "DELETE"
                       "PATCH"})
;; default responses

(def not-found
  {:status 404 :headers {} :body "not found"})

(def method-not-allowed
  {:status 405 :headers {} :body "method not allowed"})

(def server-error
  {:status 500 :headers {} :body "internal server error"})

;; default static directory handler

(defn- content-type-header [path]
  (let [content-type (get content-types (file-ext path))]
    (if (nil? content-type)
      {}
      {"Content-Type" content-type})))

(defn serve-file [path]
  {:status 200
   :headers (content-type-header path)
   :body (read-file path)})

(defn static-handler [public-dir]
  (fn [request]
    (let [path (str public-dir (:uri request))]
      (if (is-file? path)
        (serve-file path) not-found))))

;; individual route config
(defn- route [method]
  (fn [uri handler]
    {:uri uri :method method :handler handler}))

(def GET (route "GET"))

(def PUT (route "PUT"))

(def DELETE (route "DELETE"))

(def HEAD (route "HEAD"))

(def OPTIONS (route "OPTIONS"))

(defn static [static-dir]
  {:static-dir static-dir
   :method "GET"
   :handler (static-handler static-dir)})

;; route responder

(defn- static-method? [method]
  (or
   (= method "GET")
   (= method "HEAD")))

(defn- static-request? [method route]
  (and
   (static-method? method)
   (not-nil? (:static-dir route))))

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
    (catch Exception e (do (print e) server-error))))

(defn respond [routes request]
  (let [handle (match-route routes request)]
    (run-request handle request)))
