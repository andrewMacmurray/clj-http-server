(ns clj-http-server.handlers.eat-cookie
  (:require [clojure.string :as str]
            [clj-http-server.utils.function :refer :all]))

(defn- get-cookie-value [cookie]
  (-> cookie
      (str/split #"=")
      (get 1)))

(defn- type-cookie? [cookie]
  (str/starts-with? cookie "type="))

(defn- get-type-cookie [all-cookies]
  (let [find-cookie (partial find-first type-cookie?)]
    (-> all-cookies
        (str/split #";")
        (find-cookie))))

(defn- render-cookie-value [request]
  (let [cookies     (get-in request [:headers "Cookie"])
        type-cookie (get-type-cookie cookies)
        cookie-val  (get-cookie-value type-cookie)]
    (str "mmmm " cookie-val)))

(defn eat-cookie [request]
  {:status 200
   :body (render-cookie-value request)})
