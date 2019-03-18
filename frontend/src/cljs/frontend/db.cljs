(ns frontend.db
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [reagent.core :as reagent]))

(def db (reagent/atom {}))

(defn initialize-db
  "initializes the Tickets4Sale store"
  []
  (swap! db assoc :name "Tickets4Sale"))

(defn fetch-inventory
  "fetch the ticket status inventory report from the backend"
  [show-date]
  (go
   (let [response (<!
                    (http/get (str "http://localhost:8080/ticket-status/" show-date)
                              {:with-credentials? false}))]
     (swap! db assoc :inventory (:inventory (:body response))))))
