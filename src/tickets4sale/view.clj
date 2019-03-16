(ns tickets4sale.view
  (:require [tickets4sale.store :as store]
            [tickets4sale.domain :as domain]
            [clojure.data.json :as json]))

(defn ticket-status-for-show
  "return the ticket status summary for a show given a query date and chosen show date"
  [query-date show-date show]
  (let [premiere-date           (nth show 1)
        tickets-left            (domain/tickets-left query-date show-date premiere-date)
        tickets-available       (domain/tickets-available query-date show-date premiere-date)
        status                  (domain/show-status query-date show-date premiere-date)]
    {:title             (first show)
     :genre             (last show)
     :tickets-left      tickets-left
     :tickets-available tickets-available
     :status            status}))

(defn ticket-status-for-shows
  "return the ticket status summary for a list of shows given a query date and a chosen show date"
  [query-date show-date shows]
  (vec (map #(ticket-status-for-show query-date show-date %) shows)))