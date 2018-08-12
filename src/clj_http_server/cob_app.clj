(ns clj-http-server.cob-app
  (:require [clj-http-server.routing.router :as router]
            [clj-http-server.routing.route :refer :all]
            [clj-http-server.routing.responses :refer [respond-ok]]
            [clj-http-server.middleware.static :refer [with-static]]
            [clj-http-server.middleware.content-type :refer [with-content-type]]
            [clj-http-server.middleware.allowed-methods :refer [with-allowed-methods]]
            [clj-http-server.middleware.basic-auth :refer [with-basic-auth]]
            [clj-http-server.handlers.static :refer [get-static put-static delete-static]]
            [clj-http-server.handlers.options :refer [allow-default-options allow-restricted-options]]
            [clj-http-server.handlers.directory :refer [directory-links]]
            [clj-http-server.handlers.patch-content :refer [patch-content]]
            [clj-http-server.handlers.parameters :refer [parameters]]
            [clj-http-server.handlers.cat-form :refer [cat-form]]
            [clj-http-server.handlers.cookie :refer [cookie]]
            [clj-http-server.handlers.eat-cookie :refer [eat-cookie]]
            [clj-http-server.handlers.redirect :refer [redirect-home]]
            [clj-http-server.handlers.tea :refer [tea coffee]]))

(defn- cob-routes [auth-config]
  [(GET     "/coffee"            coffee)
   (GET     "/redirect"          redirect-home)
   (GET     "/tea"               tea)
   (GET     "/logs"              (with-basic-auth auth-config get-static))
   (PUT     "/new_file.txt"      put-static)
   (DELETE  "/new_file.txt"      delete-static)
   (GET     "/parameters"        parameters)
   (GET     "/cookie"            cookie)
   (GET     "/eat_cookie"        eat-cookie)
   (GET     "/cat-form/data"     cat-form)
   (PUT     "/cat-form/data"     cat-form)
   (POST    "/cat-form"          cat-form)
   (DELETE  "/cat-form/data"     cat-form)
   (PATCH   "/patch-content.txt" patch-content)
   (HEAD    "/"                  respond-ok)
   (GET     "/"                  directory-links)
   (OPTIONS "/no_file_here.txt"  allow-default-options)
   (OPTIONS "/file1"             allow-default-options)
   (OPTIONS "/logs"              allow-restricted-options)
   (static                       get-static)])

(defn- cob-handler [auth-config]
  (router/respond (cob-routes auth-config)))

(defn app-handler [public-dir auth-config]
  (->> (cob-handler auth-config)
       (with-allowed-methods)
       (with-content-type)
       (with-static public-dir)))
