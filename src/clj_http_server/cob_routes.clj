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

;; router

(defn cob-routes [public-dir]
  (let [with-static (partial middleware/with-static public-dir)]
    [(GET     "/coffee" coffee)
     (GET     "/redirect" redirect-home)
     (GET     "/tea" tea)
     (PUT     "/new_file.txt" (with-static put-static))
     (DELETE  "/new_file.txt" (with-static delete-static))
     (HEAD    "/" simple-head)
     (GET     "/" (with-static directory-links))
     (OPTIONS "/no_file_here.txt" allow-default-options)
     (OPTIONS "/file1" allow-default-options)
     (OPTIONS "/logs" log-options)
     (static  public-dir static-handler)]))
