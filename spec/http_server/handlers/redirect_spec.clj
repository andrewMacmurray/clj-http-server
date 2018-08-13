(ns http-server.handlers.redirect-spec
  (:require [speclj.core :refer :all]
            [http-server.handlers.redirect :refer :all]))

(describe "redirect-to"
          (it "creates a handler to redirect request to a path"
              (let [handler (redirect-to "/hello")
                    response (handler {})]
                (should= 302 (:status response))
                (should= "/hello" (get-in response [:headers "Location"])))))
