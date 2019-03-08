(ns tickets4sale.store
  (:require [com.stuartsierra.component :as component]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.instant :as instant]))

; in memory store
(defonce store (atom {}))

(defn normalize-row "normalizes row from data in workable format" [row]
  {:title (nth row 0)
   :date  (instant/read-instant-date (nth row 1))
   :type  (nth row 2)})

(defn initialize-from-csv "initializes store from given CSV file" [path]
  (swap! store assoc :shows
         (let [data (with-open [reader (io/reader path)]
                      (doall (csv/read-csv reader)))]
           (map #(normalize-row %) data))))

(defrecord InMemoryStore [path]

  component/Lifecycle

  (start [this]
    (assoc this :shows (initialize-from-csv path)))

  (stop [this] this))

(defn make-store [path]
  (map->InMemoryStore {:path path}))