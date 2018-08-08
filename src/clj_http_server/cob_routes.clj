(ns clj-http-server.cob-routes
  (:require [clj-http-server.router :refer :all]
            [clj-http-server.middleware :as middleware]
            [clj-http-server.utils.function :refer :all]
            [clj-http-server.utils.file :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as str]))

;; helpers

(defn to-html-link
  "creates a html link for given path"
  [path]
  (format "<a href='/%s'>%s</a>" path path))

(defn file-links
  "creates html links for each file on the path"
  [path]
  (->> (io/file path)
       (.list)
       (seq)
       (map to-html-link)
       (str/join "\n")))

(def ok {:status 200})

(defn allow [headers options]
  (assoc headers "Allow" options))

(defn allow-default [headers]
  (allow headers  "GET,HEAD,OPTIONS,PUT,DELETE"))

;; handlers

(defn respond-ok [request] ok)

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

(defn get-static [request]
  (let [handler (static-handler (:static-dir request))]
    (handler request)))

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

(def auth-config {:username "admin"
                  :password "hunter2"})

(defn cob-routes [public-dir]
  (let [with-static (partial middleware/with-static public-dir)
        with-auth   (partial middleware/with-auth auth-config)]
    [(GET     "/coffee" coffee)
     (GET     "/redirect" redirect-home)
     (GET     "/tea" tea)
     (GET     "/logs" (with-auth (with-static get-static)))
     (PUT     "/new_file.txt" (with-static put-static))
     (DELETE  "/new_file.txt" (with-static delete-static))
     (HEAD    "/" respond-ok)
     (GET     "/" (with-static directory-links))
     (OPTIONS "/no_file_here.txt" allow-default-options)
     (OPTIONS "/file1" allow-default-options)
     (OPTIONS "/logs" log-options)
     (static  public-dir)]))
