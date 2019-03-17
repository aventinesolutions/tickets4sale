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

(defn available-genres
  "available genres, sorted as keywords"
  [show-list]
  (vec (sort (map #(keyword %) (distinct (map #(:genre %) show-list))))))

(defn genre-keyword-as-str
  "converts genre keyword to str"
  [key]
  (subs (str key) 1))

(defn filter-by-genre
  "show list only for a given genre"
  [genre show-list]
  (vec
   (map #(dissoc % :genre)
        (filter #(= (genre-keyword-as-str genre) (:genre %)) show-list))))

(defn group-by-genre
  "show list as hash map grouped by genre"
  [show-list]
  (let [genres (available-genres show-list)]
    (map
     #(let [key %]
       (hash-map :genre (genre-keyword-as-str key) :shows (vec (filter-by-genre key show-list))))
     genres)))

(defn ticket-status-report
  "return a JSON report of the ticket status for shows by genre based on query date and a chosen show date"
  [query-date show-date shows]
  (let [show-list (ticket-status-for-shows query-date show-date shows)]
    (json/write-str {:inventory (vec (group-by-genre show-list))})))