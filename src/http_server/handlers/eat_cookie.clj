(ns http-server.handlers.eat-cookie
  (:require [clojure.string :as str]
            [http-server.utils.function :refer :all]))

(defn- get-cookie-value [cookie]
  (-> cookie
      (str/split #"=")
      (get 1)))

(defn- type-cookie? [cookie]
  (str/starts-with? cookie "type="))

(defn- get-type-cookie [all-cookies]
  (let [find-type-cookie (partial find-first type-cookie?)]
    (-> all-cookies
        (str/split #";")
        (find-type-cookie))))

(defn- parse-cookie [request]
  (-> request
      (get-in [:headers "Cookie"])
      (get-type-cookie)
      (get-cookie-value)))

(defn- render-cookie-value [request]
  (str "mmmm " (parse-cookie request)))

(defn eat-cookie [request]
  {:status 200
   :body (render-cookie-value request)})
