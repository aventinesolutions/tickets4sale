(ns tickets4sale.system
  (:require [com.stuartsierra.component :as component]
            [tickets4sale.server :as server]
            [tickets4sale.store :as store]
            [clojure.tools.logging :refer [error]]))

(def ^:redef system
  "reference to the Tickets4Sale backend system"
  nil)

(defn build-system
  "defines Tickets4Sale system component map"
  []
  (try
    (-> (component/system-map
         :server (server/make-server)
         :store store/store)
        (component/system-using {:server [:store]}))
    (catch Exception e
      (error "Failed to build Tickets4sale system" e))))

(defn init-system
  []
  (let [sys (build-system)]
    (alter-var-root #'system (constantly sys))))

(defn stop!
  "Stop Tickets4Sale backend system"
  []
  (alter-var-root #'system component/stop-system))

(defn start!
  "Start Tickets4Sale backend system"
  []
  (alter-var-root #'system component/start-system)
  (println "Tickets4sale system started"))