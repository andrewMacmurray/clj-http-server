(ns clj-http-server.router
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clj-http-server.constants :refer [content-types]]
            [clj-http-server.utils :refer :all])
  (:import [java.io ByteArrayOutputStream]
           [java.nio.file Paths Files]))

;; default responses

(def not-found
  {:status 404 :headers {} :body "not found"})

(def server-error
  {:status 500 :headers {} :body "internal server error"})

;; default static directory handler

(defn read-file [path]
  (Files/readAllBytes (.toPath (io/file path))))

(defn- content-type-header [path]
  (let [content-type (get content-types (file-ext path))]
    (if (nil? content-type)
      {}
      {"Content-Type" content-type})))

(defn- serve-file [path]
  {:status 200
   :headers (content-type-header path)
   :body (read-file path)})

(defn is-file? [path]
  (let [file (io/file path)]
    (and
     (.exists file)
     (not (.isDirectory file)))))

(defn static-handler [public-dir]
  (fn [request]
    (let [path (str public-dir (:uri request))]
      (if (is-file? path)
        (serve-file path) not-found))))

;; individual route config

(defn GET [uri handler]
  {:uri uri :method "GET" :handler handler})

(defn HEAD [uri handler]
  {:uri uri :method "HEAD" :handler handler})

(defn OPTIONS [uri handler]
  {:uri uri :method "OPTIONS" :handler handler})

(defn static [static-dir handler]
  {:static-dir static-dir :method "GET" :handler (handler static-dir)})

;; route responder

(defn- static-request? [method route]
  (and
   (= method "GET")
   (not-nil? (:static-dir route))))

(defn- matching-route? [uri method route]
  (and
   (= (:uri route) uri)
   (= (:method route) method)))

(defn- find-handler [routes request]
  (let [uri (:uri request)
        request-method (:method request)
        route-match  (partial matching-route? uri request-method)
        static-match (partial static-request? request-method)]
    (or
     (find-first route-match routes)
     (find-first static-match routes))))

(defn- run-request [route request]
  (try
    (if (nil? route) not-found ((:handler route) request))
    (catch Exception e (do
                         (print e)
                         server-error))))

(defn respond [routes request]
  (let [handle (find-handler routes request)]
    (run-request handle request)))
