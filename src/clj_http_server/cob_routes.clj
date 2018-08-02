(ns clj-http-server.cob-routes
  (:require [clj-http-server.router :refer :all]
            [clojure.java.io :as io]))

(defn simple-head [request]
  {:status 200
   :headers {}
   :body ""})

(defn file1-option [request]
  {:status 200
   :headers {"Allow" "GET,HEAD,OPTIONS,PUT,DELETE"}
   :body ""})

(defn no-file-here-options [request]
  {:status 200
   :headers {"Allow" "GET,HEAD,OPTIONS,PUT,DELETE"}
   :body ""})

(defn log-options [request]
  {:status 200
   :headers {"Allow" "GET,HEAD,OPTIONS"}
   :body ""})

(defn coffee [request]
  {:status 418
   :headers {}
   :body "I'm a teapot"})

(defn tea [request]
  {:status 200
   :headers {}
   :body ""})

(defn redirect [request]
  {:status 302
   :headers {"Location" "/"}
   :body ""})

(defn cob-routes [public-dir]
  [(GET     "/coffee" coffee)
   (GET     "/redirect" redirect)
   (GET     "/tea" tea)
   (HEAD    "/" simple-head)
   (OPTIONS "/no_file_here.txt" no-file-here-options)
   (OPTIONS "/logs" log-options)
   (OPTIONS "/file1" file1-option)
   (static  public-dir static-handler)])
