; the business domain
(ns tickets4sale.domain
  (:require [java-time :as time]))

(def show-run-in-days (time/days 100))

(def move-to-smaller-venue-after-days 60)

(def larger-venue-capacity-seats 200)

(def smaller-venue-capacity-seats 100)

(def sales-start-days-before-opening (time/days 25))

(def tickets-sold-per-day-bigger-venue 10)

(def tickets-sold-per-day-smaller-venue 5)

(defn ticket-sales-start
  "the day that ticket sales will/did start"
  [show-opening]
  (time/minus show-opening sales-start-days-before-opening))

(defn ticket-sales-started?
  "have the ticket sales started?"
  [query-date show-opening]
  (let [sales-start (ticket-sales-start show-opening)]
    (or (= query-date sales-start)
        (time/after? query-date sales-start))))

(defn venue-type
  "the venue type based on the number of days the show has been running"
  [show-date show-opening])

(defn tickets-left
  "the number of tickets left based on days running"
  [show-date show-opening])

(defn show-status
  "the status of the show based on query date and when the show opens"
  [query-date show-opening]
  (if (not (ticket-sales-started? query-date show-opening))
    "sale not started"
    "unknown"))

(defn tickets-available
  "the number of tickets available based on tickets that should have been sold"
  [show-date show-opening])

(defn tickets-sold
  "the number of tickets presumable sold"
  [show-date show-opening])