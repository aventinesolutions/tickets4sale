; the business domain
(ns tickets4sale.domain
  (:require [java-time :as jt]))

(def show-run-in-days (jt/days 100))

(def move-to-smaller-venue-after-days (jt/days 60))

(def larger-venue-capacity-seats 200)

(def smaller-venue-capacity-seats 100)

(def sales-start-days-before-show (jt/days 25))

(def tickets-sold-per-day-bigger-venue 10)

(def tickets-sold-per-day-smaller-venue 5)

(defn number-of-days-between
  "the number of days between two dates; could be negative if end date is before start"
  [start-date end-date]
  (-> (java.time.temporal.ChronoUnit/DAYS) (.between start-date end-date)))

(defn ticket-sales-start
  "the day that ticket sales will/did start for a show"
  [show-date]
  (jt/minus show-date sales-start-days-before-show))

(defn ticket-sales-started?
  "have the ticket sales started for this show?"
  [query-date show-date]
  (let [sales-start (ticket-sales-start show-date)]
    (not (jt/before? query-date sales-start))))

(defn in-smaller-venue?
  "is the show performance in the smaller venue? (based on show premiere dates)"
  [show-date premiere-date]
  (not
    (jt/before? show-date (jt/plus premiere-date move-to-smaller-venue-after-days))))

(defn capacity
  "return total capicity based on show and premiere dates"
  [show-date premiere-date]
  (if
    (in-smaller-venue? show-date premiere-date)
    smaller-venue-capacity-seats
    larger-venue-capacity-seats))

(defn sold-per-day
  "return tickets sold per day based show and premiere dates"
  [show-date premiere-date]
  (if
    (in-smaller-venue? show-date premiere-date)
    tickets-sold-per-day-smaller-venue
    tickets-sold-per-day-bigger-venue))

(defn tickets-sold
  "returns the tickets sold based on the query, show and premiere dates"
  [query-date show-date premiere-date]
  (let [capacity (capacity show-date premiere-date)]
    (cond
      (not (ticket-sales-started? query-date show-date))     0
      (jt/after? query-date show-date)                       capacity
      :else
      (min capacity
           (*
            (inc (- (number-of-days-between query-date (jt/minus show-date sales-start-days-before-show))))
            (sold-per-day show-date premiere-date))))))

(defn tickets-left
  "returns the tickets left based on the query, show and premiere dates"
  [query-date show-date premiere-date]
  (- (capacity show-date premiere-date) (tickets-sold query-date show-date premiere-date)))

(defn show-status
  "the status of the show based on query date and when the show opens"
  [query-date show-date]
  (if-not (ticket-sales-started? query-date show-date)
    "sale not started"
    "unknown"))
