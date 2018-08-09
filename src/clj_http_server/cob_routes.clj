(ns clj-http-server.cob-routes
  (:require [clj-http-server.routing.router :refer :all]
            [clj-http-server.routing.response :refer :all]
            [clj-http-server.routing.route :refer :all]
            [clj-http-server.middleware :as middleware]
            [clj-http-server.handlers.static :refer :all]
            [clj-http-server.handlers.options :refer :all]
            [clj-http-server.handlers.directory :refer :all]
            [clj-http-server.handlers.parameters :refer :all]
            [clj-http-server.handlers.cookie :refer :all]
            [clj-http-server.handlers.eat-cookie :refer :all]
            [clj-http-server.handlers.redirect :refer :all]
            [clj-http-server.handlers.tea :refer :all]))

(defn- STATIC [public-dir]
  (static public-dir get-static))

(defn cob-routes [public-dir auth-config]
  (let [with-static (partial middleware/with-static public-dir)
        with-auth   (partial middleware/with-auth auth-config)]
    [(GET     "/coffee"           coffee)
     (GET     "/redirect"         redirect-home)
     (GET     "/tea"              tea)
     (GET     "/logs"             (with-auth (with-static get-static)))
     (PUT     "/new_file.txt"     (with-static put-static))
     (DELETE  "/new_file.txt"     (with-static delete-static))
     (GET     "/parameters"       parameters)
     (GET     "/cookie"           cookie)
     (GET     "/eat_cookie"       eat-cookie)
     (HEAD    "/"                 respond-ok)
     (GET     "/"                 (with-static directory-links))
     (OPTIONS "/no_file_here.txt" allow-default-options)
     (OPTIONS "/file1"            allow-default-options)
     (OPTIONS "/logs"             allow-restricted-options)
     (STATIC  public-dir)]))

(defn cob-app [public-dir auth-config]
  (fn [request]
    (respond (cob-routes public-dir auth-config) request)))
