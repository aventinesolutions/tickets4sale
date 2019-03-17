(ns tickets4sale.store
  (:require [com.stuartsierra.component :as component]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [java-time :as jt]))

; in memory store
(defonce store (atom {}))

(defn normalize-row "normalizes row from data in workable format" [row]
  [(first row) (jt/local-date (nth row 1)) (last row)])

(defn initialize-from-csv "initializes store from given CSV file" [path]
  (swap! store assoc :shows
         (let [data (with-open [reader (io/reader path)]
                      (doall (csv/read-csv reader)))]
           (map #(normalize-row %) data))))
