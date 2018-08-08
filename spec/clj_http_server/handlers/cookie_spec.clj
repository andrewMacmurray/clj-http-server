(ns clj-http-server.handlers.cookie-spec
  (:require [speclj.core :refer :all]
            [clj-http-server.handlers.cookie :refer :all]))

(describe "cookie"
          (it "sets a cookie in the response based on the request params"
              (let [request {:params {"type" "cookie"}}
                    response (cookie request)]
                (should= {"Set-Cookie" "type=cookie"} (:headers response))))

          (it "responds with Eat"
              (let [request {:params {"type" "cookie"}}
                    response (cookie request)]
                (should= "Eat" (:body response)))))
