(ns tickets4sale.system
  (:require [com.stuartsierra.component :as component]
            [tickets4sale.store :as store]
            [clojure.tools.logging :refer [error]]))

(def ^:redef cli-system "The CLI system." nil)

(defn build-cli-system "Defines CLI system map." [path]
  (try
    (component/system-map :store (store/make-store path))
    (catch Exception e
      (error "Failed to build CLI system" e))))

(defn init-cli-system [path]
  (let [sys (build-cli-system path)]
    (alter-var-root #'cli-system (constantly sys))))

(defn stop! "Stop CLI system" []
  (alter-var-root #'cli-system component/stop-system))

(defn start! "Start CLI system" [path]
  (alter-var-root #'cli-system component/start-system path)
  (println "CLI System started"))