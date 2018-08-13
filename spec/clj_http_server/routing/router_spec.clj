(ns clj-http-server.routing.router-spec
  (:require [speclj.core :refer :all]
            [clj-http-server.utils.file :refer :all]
            [clj-http-server.routing.responses :refer :all]
            [clj-http-server.routing.route :refer :all]
            [clj-http-server.routing.router :refer :all]))

(defn foo-handler [request]
  {:status 200
   :headers {}
   :body (:method request)})

(defn bar-handler [_]
  {:status 204
   :headers {}
   :body "bar"})

(defn foo-options-handler [_]
  {:status 200
   :headers {"Allow" "GET,PUT"}
   :body ""})

(defn get-static [_]
  {:status 200
   :headers {}
   :body "public/file1"})

(def routes
  [(OPTIONS "/foo" foo-options-handler)
   (GET "/bar" bar-handler)
   (GET "/foo" foo-handler)
   (static get-static)])

(def foo-request     {:method "GET" :uri "/foo"})
(def foo-options     {:method "OPTIONS" :uri "/foo"})
(def foo-delete      {:method "DELETE" :uri "/foo"})
(def bar-request     {:method "GET" :uri "/bar"})
(def static-file     {:method "GET" :uri "/file1" :static-dir "public"})

(def response-handler (respond routes))

(describe "GET"
          (it "associates a handler with a uri and http GET method"
              (let [route (GET "/foo" foo-handler)
                    expected {:uri "/foo" :method "GET" :handler foo-handler}]
                (should= route expected))))

(describe "respond"
          (it "creates correct response for /foo request"
              (let [response (response-handler foo-request)]
                (should= (foo-handler foo-request) response)))

          (it "creates correct response for /bar request"
              (let [response (response-handler bar-request)]
                (should= (bar-handler bar-request) response)))

          (it "creates correct response for /foo options request"
              (let [response (response-handler foo-options)]
                (should= (foo-options-handler foo-options) response)))

          (it "creates a method not allowed response if uri maches not allowed OPTIONS"
              (let [response (response-handler foo-delete)]
                (should= method-not-allowed response)))

          (it "matches the static handler if a specific route is not matched"
              (let [response (response-handler static-file)]
                (should= (get-static static-file) response))))
