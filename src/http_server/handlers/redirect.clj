(ns http-server.handlers.redirect)

(defn redirect-to [path]
  (fn [_]
    {:status 302 :headers {"Location" path}}))

(def redirect-home
  (redirect-to "/"))
