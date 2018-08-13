(ns http-server.routing.route)

(defn- make-route [method]
  (fn [uri handler]
    {:method method
     :uri uri
     :handler handler}))

(def GET (make-route "GET"))

(def PUT (make-route "PUT"))

(def POST (make-route "POST"))

(def PATCH (make-route "PATCH"))

(def DELETE (make-route "DELETE"))

(def HEAD (make-route "HEAD"))

(def OPTIONS (make-route "OPTIONS"))

(defn static [static-handler]
  {:static-request true
   :method "GET"
   :handler static-handler})
