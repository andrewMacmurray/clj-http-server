(ns clj-http-server.routes.redirect)

(defn redirect-to [path]
  (fn [_]
    {:status 302 :headers {"Location" path}}))

(def redirect-home
  (redirect-to "/"))
