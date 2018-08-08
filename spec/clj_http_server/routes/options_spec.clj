(ns clj-http-server.routes.options-spec
  (:require [speclj.core :refer :all]
            [clj-http-server.routes.options :refer :all]))

(describe "allow-default-options"
          (it "responds with default options in allow header"
              (let [response (allow-default-options {})]
                (should= 200 (:status response))
                (should= (allow {} "GET,HEAD,OPTIONS,PUT,DELETE") (:headers response)))))

(describe "allow-restricted-options"
          (it "responds with restricted otpions in allow header"
              (let [response (allow-restricted-options {})]
                (should= 200 (:status response))
                (should= (allow {} "GET,HEAD,OPTIONS") (:headers response)))))
