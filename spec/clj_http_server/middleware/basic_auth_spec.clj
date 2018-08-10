(ns clj-http-server.middleware.basic-auth-spec
  (:require [clj-http-server.middleware.basic-auth :refer :all]
            [clj-http-server.utils.base64 :refer :all]
            [speclj.core :refer :all]))

(defn handler [request]
  {:status 200
   :body (:static-dir request)})

(def auth-config {:username "admin" :password "hunter2"})

(describe "with-auth"
          (it "lets request through if it contains correct authentication headers"
              (let [new-handler (with-basic-auth auth-config handler)
                    valid-credentials (str "Basic " (encode "admin:hunter2"))
                    response (new-handler {:headers {"Authorization" valid-credentials}})]
                (should= (handler {}) response)))

          (it "rejects request without correct authentication headers"
              (let [new-handler (with-basic-auth auth-config handler)
                    response (new-handler {})]
                (should= 401 (:status response)))))
