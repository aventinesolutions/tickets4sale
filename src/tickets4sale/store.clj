(ns tickets4sale.store
  (:require [com.stuartsierra.component :as component]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.instant :as instant]))

(defn initialize-from-csv "initializes store from given CSV file" [path]
  (atom
   (with-open [reader (io/reader path)]
     (map
      (fn [row]
        {:title (nth row 0)
         :date  (instant/read-instant-date (nth row 1))
         :type  (nth row 2)})
      (doall
       (csv/read-csv reader))))))

(defrecord InMemoryStore [path]

  component/Lifecycle

  (start [this]
    (assoc this :shows (initialize-from-csv path)))

  (stop [this] this))

(defn make-store [path]
  (map->InMemoryStore {:path path}))