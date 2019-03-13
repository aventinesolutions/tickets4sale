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

(defn show-status
  "the status of the show based on query date and when the show opens"
  [query-date show-date]
  (if (not (ticket-sales-started? query-date show-date))
    "sale not started"
    "unknown"))
