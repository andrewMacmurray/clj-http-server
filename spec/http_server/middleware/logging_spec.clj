(ns http-server.middleware.logging-spec
  (:require [speclj.core :refer :all]
            [clj-http-server.utils.logging :refer :all]
            [clj-http-server.middleware.logging :refer :all]))

(defn handler [request]
  {:status 200})

(def modified-handler
  (with-logs handler))

(describe "with-logs"
          (with-stubs)
          (around [it] (with-redefs [log-request (stub :log-request-stub)]))
          (it "calls the logger with the request when handler is called"
              (let [request {:uri "/hello"}
                    _ (modified-handler request)]
                (should-have-invoked :log-request-stub {:with [request]})))

          (it "ignores logs when request uri is /"
              (let [request {:uri "/"}
                    _ (modified-handler request)]
                (should-not-have-invoked :log-request-stub)))

          (it "does not modify the request or response"
              (let [request {:uri "/hello"}
                    response (modified-handler)]
                (should= (handler request) response))))
