(ns clj-http-server.handlers.cat-form)

(def form-data (atom nil))

(defn- location [{uri :uri}]
  {"Location" (str uri "/data")})

(defn- handle-post [request form-data]
  (reset! form-data (:body request))
  {:status 201
   :headers (location request)})

(defn- handle-put [request form-data]
  (reset! form-data (:body request))
  {:status 200})

(defn- handle-get [form-data]
  (if @form-data
    {:status 200 :body @form-data}
    {:status 404 :body "not found"}))

(defn- handle-delete [form-data]
  (reset! form-data nil)
  {:status 200})

(defn cat-form [request]
  (case (:method request)
    "GET"    (handle-get form-data)
    "POST"   (handle-post request form-data)
    "PUT"    (handle-put request form-data)
    "DELETE" (handle-delete form-data)))
