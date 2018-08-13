(ns http-server.handlers.cat-form-spec
  (:require [speclj.core :refer :all]
            [http-server.handlers.cat-form :refer :all]))

(describe "cat-form"
          (it "responds with 404 when getting data from empty form"
              (let [response (cat-form {:method "GET"})]
                (should= 404 (:status response))))

          (it "responds with a 201 when data successfully posted to form"
              (let [response (cat-form {:method "POST" :uri "/cat-form" :body "data=fatcat"})]
                (should= 201 (:status response))
                (should= {"Location" "/cat-form/data"} (:headers response))))

          (it "fetches the data from the form after posting"
              (let [_ (cat-form {:method "POST" :uri "/cat-form/data" :body "data=fatcat"})
                    response (cat-form {:method "GET"})]
                (should= 200 (:status response))
                (should= "data=fatcat" (:body response))))

          (it "fetches the data from the form after putting"
              (let [put-res (cat-form {:method "PUT" :uri "/cat-form/data" :body "data=fatcat"})
                    get-res (cat-form {:method "GET"})]
                (should= 200 (:status put-res))
                (should= 200 (:status get-res))
                (should= "data=fatcat" (:body get-res))))

          (it "deletes form data correctly"
              (let [_ (cat-form {:method "POST" :uri "/cat-form/data" :body "data=fatcat"})
                    delete-res (cat-form {:method "DELETE"})
                    get-res (cat-form {:method "GET"})]
                (should= 200 (:status delete-res))
                (should= 404 (:status get-res)))))
