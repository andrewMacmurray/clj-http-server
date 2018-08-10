(ns clj-http-server.routing.responses)

(def not-found
  {:status 404 :headers {} :body "not found"})

(def method-not-allowed
  {:status 405 :headers {} :body "method not allowed"})

(def precondition-failed
  {:status 412 :headers {} :body "precondition failed"})

(def server-error
  {:status 500 :headers {} :body "internal server error"})

(defn respond-ok [_]
  {:status 200})
