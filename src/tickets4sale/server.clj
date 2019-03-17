(ns tickets4sale.server
  (:require [com.stuartsierra.component :as component]
            [java-time :as jt]
            [bidi.ring :refer [make-handler]]
            [bidi.bidi :as bidi]
            [aleph.http :as http]
            [ring.util.response :as response]
            [ring.util.request :as request]
            [ring.middleware.params :refer [wrap-params]]
            [tickets4sale.view :as view]
            [tickets4sale.store :as store]))

(def routes ["/" {"ticket-status/" {[:show-date] :report}}])

(defn handle-default
  "handle default page"
  [request]
  (response/response (view/render-default)))

(defn default-handler
  "handle GET request for the default page"
  [store request]
  (handle-default request))

(defn report-handler
  "handle GET request for a ticket status report given a show date"
  [store request]
  (swap! store assoc :show-date (jt/local-date (:show-date (:params request))))
  (let [query-date (:query-date @store)
        show-date  (:show-date @store)
        shows      (:shows @store)]
    (let [response (-> (response/response (view/ticket-status-report query-date show-date shows))
                       (response/header "Access-Control-Allow-Origin" "*")
                       (response/content-type "application/json")
                       (response/status 200))]
      (println response) response)))

(defn handler
  "Get the handler function for our routes."
  [store]
  (make-handler
   ["/"
    {""                            (partial default-handler store)
     ["ticket-status/" :show-date] (partial report-handler store)}]))

(defn app
  [store]
  (-> (handler store)
      wrap-params))

(defrecord HttpServer [server]

  component/Lifecycle

  (start [this]
    (assoc this :server (http/start-server (app (:store this)) {:port 8080})))

  (stop [this]
    (dissoc this :server)))

(defn make-server
  []
  (map->HttpServer {}))
