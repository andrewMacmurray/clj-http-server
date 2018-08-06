(ns clj-http-server.cob-routes
  (:require [clj-http-server.router :refer :all]
            [clj-http-server.middleware :as middleware]
            [clj-http-server.utils :refer :all]
            [clojure.java.io :as io]))

;; helpers

(def ok {:status 200})

(defn allow [headers options]
  (assoc headers "Allow" options))

(defn allow-default [headers]
  (allow headers  "GET,HEAD,OPTIONS,PUT,DELETE"))

;; handlers

(defn simple-head [request] ok)

(defn allow-default-options [request]
  {:status 200
   :headers (allow-default {})})

(defn log-options [request]
  {:status 200
   :headers (allow {} "GET,HEAD,OPTIONS")})

(defn coffee [request]
  {:status 418
   :body "I'm a teapot"})

(defn tea [request] ok)

(defn redirect-home [request]
  {:status 302
   :headers {"Location" "/"}})

(defn directory-links [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (file-links (:static-dir request))})

;; router

(defn cob-routes [public-dir]
  [(GET     "/coffee" coffee)
   (GET     "/redirect" redirect-home)
   (GET     "/tea" tea)
   (HEAD    "/" simple-head)
   (GET     "/" (middleware/with-static public-dir directory-links))
   (OPTIONS "/no_file_here.txt" allow-default-options)
   (OPTIONS "/file1" allow-default-options)
   (OPTIONS "/logs" log-options)
   (static  public-dir static-handler)])
