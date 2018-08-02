(ns clj-http-server.middleware-spec
  (:require [speclj.core :refer :all]
            [clj-http-server.middleware :as middleware]))

(defn foo-handler [request]
  {:status 200
   :headers {}
   :body (:static-dir request)})

(describe "add public dir"
          (it "adds the public directory to a handlers request"
              (let [new-handler (middleware/with-static-dir "my-public-dir" foo-handler)
                    response (new-handler {})]
                (should= "my-public-dir" (:body response)))))
