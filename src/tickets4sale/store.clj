(ns tickets4sale.store
  (:require [com.stuartsierra.component :as component]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn initialize-from-csv "initializes store from given CVS file" [path]
  (atom
    (with-open [reader (io/reader path)]
      (doall
       (csv/read-csv reader)))))

(defrecord InMemoryStore [path]

  component/Lifecycle

  (start [this]
    (assoc this :shows (initialize-from-csv path)))

  (stop [this] this))

(defn make-store [path]
  (map->InMemoryStore {:path path}))