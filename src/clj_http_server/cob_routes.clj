(ns clj-http-server.cob-routes
  (:require [clj-http-server.router :refer :all]
            [clj-http-server.middleware :as middleware]))

(defn serve-file [request filename]
  (let [dir (:static-dir request)]
    (slurp (str dir filename))))

(defn file1-handler [request]
  {:status 200
   :headers {}
   :body (serve-file request "/file1")})

(defn file2-handler [request]
  {:status 200
   :headers {}
   :body (serve-file request "/file2")})

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

(defn cob-routes [public-dir]
  (let [static (partial middleware/with-static-dir public-dir)]
    [(GET     "/file1" (static file1-handler))
     (GET     "/file2" (static file2-handler))
     (HEAD    "/" simple-head)
     (OPTIONS "/no_file_here.txt" no-file-here-options)
     (OPTIONS "/logs" log-options)
     (OPTIONS "/file1" file1-option)]))
