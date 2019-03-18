(ns frontend.db
  (:require [reagent.core :as reagent]))

(def db (reagent/atom {}))

(defn initialize-db
  "initializes the Tickets4Sale store"
  []
  (swap! db assoc :name "Tickets4Sale"))
