(ns clj-http-server.middleware.allowed-methods-spec
  (:require [speclj.core :refer :all]
            [clj-http-server.middleware.allowed-methods :refer :all]))

(defn handler [request]
  {:status 200
   :headers {}
   :body "hello"})

(def modified-handler (with-allowed-methods handler))

(describe "with-allowed-methods"
          (it "runs the original handler if the method is recognised by the server"
              (let [response (modified-handler {:method "GET"})]
                (should= 200 (:status response))
                (should= "hello" (:body response))))

          (it "responds with a not allowed response if method is unrecognised by the server"
              (let [response (modified-handler {:method "FOO"})]
                (should= 405 (:status response)))))
