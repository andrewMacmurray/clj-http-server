(ns clj-http-server.handlers.eat-cookie-spec
  (:require [speclj.core :refer :all]
            [clj-http-server.handlers.eat-cookie :refer :all]))

(describe "eat cookie"
          (it "responds with mmmm plus the value of the type cookie"
              (let [request {:headers {"Cookie" "type=twiglets"}}
                    response (eat-cookie request)]
                (should= "mmmm twiglets" (:body response)))))
