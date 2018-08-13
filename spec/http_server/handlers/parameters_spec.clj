(ns http-server.handlers.parameters-spec
  (:require [speclj.core :refer :all]
            [http-server.handlers.parameters :refer :all]))

(describe "parameters"
          (it "renders query params to response body"
              (let [request {:params {"Foo" "Bar" "Bar" "Foo"}}
                    response (parameters request)]
                (should= "Foo = Bar\nBar = Foo" (:body response)))))
