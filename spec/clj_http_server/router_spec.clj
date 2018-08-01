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

(def routes {"/foo" foo-handler
             "/bar" bar-handler})

(def foo-request     {:method "GET"  :uri "/foo"})
(def bar-request     {:method "POST" :uri "/bar"})
(def unknown-request {:method "GET"  :uri "/unknown"})

(describe "respond"
          (it "creates correct response for /foo request"
              (let [response (respond routes foo-request)]
                (should= (foo-handler foo-request) response)))

          (it "creates correct response for /bar request"
              (let [response (respond routes bar-request)]
                (should= (bar-handler bar-request) response)))

          (it "creates a 404 response if route not found"
              (let [response (respond routes unknown-request)]
                (should= {:status 404 :headers {} :body "not found"} response))))
