(ns clj-http-server.routes.tea)

(defn coffee [request]
  {:status 418
   :body "I'm a teapot"})

(defn tea [request]
  {:status 200})
