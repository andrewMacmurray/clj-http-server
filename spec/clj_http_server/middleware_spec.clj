(ns clj-http-server.middleware-spec
  (:require [clj-http-server.middleware :as middleware]
            [speclj.core :refer :all]))

(defn handler [request]
  {:status 200
   :body (:static-dir request)})

(describe "with-static"
          (it "adds static content directory to a reqest"
              (let [new-handler (middleware/with-static "public" handler)
                    response (new-handler {})]
                (should= "public" (:body response)))))
