(ns clj-http-server.middleware.basic-auth
  (:require [clojure.string :as str]
            [clj-http-server.utils.encoding :refer :all]))

(defn- parse-basic [credentials]
  (-> credentials
      (str/split #"Basic ")
      (get 1 "")
      (decode)
      (str/split #":" 2)))

(defn- is-authenticated? [auth-config request]
  (let [auth-username       (:username auth-config)
        auth-password       (:password auth-config)
        auth-header         (get-in request [:headers "Authorization"] "")
        [username password] (parse-basic auth-header)]
    (and
     (= username auth-username)
     (= password auth-password))))

(def not-authorized
  {:status 401
   :headers {"WWW-Authenticate" "Basic realm='authentication'"}})

(defn with-basic-auth [auth-config handler]
  (fn [request]
    (if (is-authenticated? auth-config request)
      (handler request) not-authorized)))
