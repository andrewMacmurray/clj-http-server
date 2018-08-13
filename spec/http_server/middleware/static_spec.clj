(ns http-server.middleware.static-spec
  (:require [http-server.middleware.static :as middleware]
            [speclj.core :refer :all]))

(defn handler [request]
  {:status 200
   :body (:static-dir request)})

(describe "with-static-dir"
          (it "adds static content directory to a reqest"
              (let [new-handler (middleware/with-static-dir "public" handler)
                    response (new-handler {})]
                (should= "public" (:body response)))))
