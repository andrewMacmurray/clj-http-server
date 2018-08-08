(ns clj-http-server.handlers.options)

(defn allow [headers options]
  (assoc headers "Allow" options))

(defn allow-default [headers]
  (allow headers  "GET,HEAD,OPTIONS,PUT,DELETE"))

(defn allow-default-options [request]
  {:status 200
   :headers (allow-default {})})

(defn allow-restricted-options [request]
  {:status 200
   :headers (allow {} "GET,HEAD,OPTIONS")})
