(ns clj-http-server.router-spec
  (:require [speclj.core :refer :all]
            [clj-http-server.router :refer :all]))

(defn foo-handler [request]
  {:status 200
   :headers {}
   :body (:method request)})

(defn bar-handler [request]
  {:status 204
   :headers {}
   :body "bar"})

(defn foo-options-handler [request]
  {:status 200
   :headers {"Allow" "GET,PUT"}
   :body ""})

(def routes
  [(OPTIONS "/foo" foo-options-handler)
   (GET "/bar" bar-handler)
   (GET "/foo" foo-handler)])

(def foo-request     {:method "GET" :uri "/foo"})
(def foo-options     {:method "OPTIONS" :uri "/foo"})
(def bar-request     {:method "GET" :uri "/bar"})
(def unknown-request {:method "GET" :uri "/unknown"})

(describe "GET"
          (it "associates a handler with a uri and http GET method"
              (let [route (GET "/foo" foo-handler)
                    expected {:uri "/foo" :method "GET" :handler foo-handler}]
                (should= route expected))))

(describe "respond"
          (it "creates correct response for /foo request"
              (let [response (respond routes foo-request)]
                (should= (foo-handler foo-request) response)))

          (it "creates correct response for /bar request"
              (let [response (respond routes bar-request)]
                (should= (bar-handler bar-request) response)))

          (it "creates correct response for /foo options request"
              (let [response (respond routes foo-options)]
                (should= (foo-options-handler foo-options) response)))

          (it "creates a 404 response if route not found"
              (let [response (respond routes unknown-request)]
                (should= {:status 404 :headers {} :body "not found"} response))))
