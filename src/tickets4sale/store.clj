(ns tickets4sale.store
  (:require [com.stuartsierra.component :as component]))

(defrecord InMemoryStore [data]

  component/Lifecycle

  (start [this]
    (assoc this :shows (atom {})))

  (stop [this] this))

(defn make-store
  []
  (map->InMemoryStore {}))