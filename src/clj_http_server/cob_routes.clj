(ns clj-http-server.cob-routes
  (:require [clj-http-server.middleware :as middleware]))

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

(defn cob-routes [public-dir]
  (let [static (partial middleware/with-static-dir public-dir)]
    {"/file1" (static file1-handler)
     "/file2" (static file2-handler)}))
