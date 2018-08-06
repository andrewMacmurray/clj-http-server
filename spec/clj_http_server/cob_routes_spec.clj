(ns clj-http-server.cob-routes-speci
  (:require [clj-http-server.cob-routes :refer :all]
            [speclj.core :refer :all]))


(describe "simple head"
          (it "returns 200 for request"
              (let [response (simple-head {})]
                (should= 200 (:status response)))))

(describe "allow-default-options"
          (it "responds with default options in allow header"
              (let [response (allow-default-options {})]
                (should= 200 (:status response))
                (should= (allow-default {}) (:headers response)))))

(describe "redirect-home"
          (it "redirects the request to /"
              (let [response (redirect-home {})]
                (should= 302 (:status response))
                (should= {"Location" "/"} (:headers response)))))
