; the business domain
(ns tickets4sale.domain
  (:require [java-time :as time]
            [clojure.instant :as instant]))

(def show-run-in-days 100)

(def move-to-smaller-venue-after-days 60)

(def larger-venue-capacity-seats 200)

(def smaller-venue-capacity-seats 100)

(def sales-start-days-before-opening 25)

(def tickets-sold-per-day-bigger-venue 10)

(def tickets-sold-per-day-smaller-venue 5)

(defn ticket-sales-started?
  "have the ticket sales started?"
  [query-date show-start]

  )

(defn ticket-sales-start
  "the day that ticket sales will/did start"
  [query-date show-start]
  )

(defn venue-type
  "the venue type based on the number of days the show has been running"
  [show-start])

(defn tickets-left
  "the number of tickets left based on days running"
  [show-start])

(defn show-status
  "the status of the show based on start and days running"
  [show-start])

(defn tickets-available
  "the number of tickets available based on tickets that should have been sold"
  [show-start])

(defn tickets-sold
  "the number of tickets presumable sold"
  [show-start])