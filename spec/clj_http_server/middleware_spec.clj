(ns clj-http-server.middleware-spec
  (:require [clj-http-server.middleware :as middleware]
            [clj-http-server.utils.encoding :refer :all]
            [speclj.core :refer :all]))

(defn handler [request]
  {:status 200
   :body (:static-dir request)})

(def auth-config {:username "admin" :password "hunter2"})

(describe "with-static"
          (it "adds static content directory to a reqest"
              (let [new-handler (middleware/with-static "public" handler)
                    response (new-handler {})]
                (should= "public" (:body response)))))

(describe "with-auth"
          (it "lets request through if it contains correct authentication headers"
              (let [new-handler (middleware/with-auth auth-config handler)
                    valid-credentials (str "Basic " (encode "admin:hunter2"))
                    response (new-handler {:headers {"Authorization" valid-credentials}})]
                (should= (handler {}) response)))

          (it "rejects request without correct authentication headers"
              (let [new-handler (middleware/with-auth auth-config handler)
                    response (new-handler {})]
                (should= 401 (:status response)))))
