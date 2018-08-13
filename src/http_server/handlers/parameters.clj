(ns http-server.handlers.parameters
  (:require [clojure.string :as str]))

(defn- param-to-string [[k v]]
  (str k " = " v))

(defn- render-query-params [params]
  (->> params
       (seq)
       (map param-to-string)
       (str/join "\n")))

(defn parameters [request]
  {:status 200
   :body (render-query-params (:params request))})
