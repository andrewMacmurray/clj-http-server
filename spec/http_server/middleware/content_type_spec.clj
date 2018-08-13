(ns http-server.middleware.content-type-spec
  (:require [speclj.core :refer :all]
            [http-server.middleware.content-type :refer :all]))

(defn handler [request]
  {:status 200
   :headers (:extra-headers request)})

(def modified-handler (with-content-type handler))

(describe "with-content-type"
          (it "adds a content type header for matching file extension"
              (let [response (modified-handler {:method "GET" :uri "/hello.txt"})]
                (should= 200 (:status response))
                (should= {"Content-Type" "text/plain"} (:headers response))))

          (it "adds no headers if no matching content type"
              (let [response (modified-handler {:uri "/hello"})]
                (should= nil (:headers response))))

          (it "adds to existing headers if matching file extension found"
              (let [response (modified-handler {:uri "/hello.png" :extra-headers {"Foo" "Bar"}})]
                (should= {"Foo" "Bar" "Content-Type" "image/png"} (:headers response)))))
