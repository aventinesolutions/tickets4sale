(ns frontend.core
  (:require
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [frontend.events :as events]
    [frontend.views :as views]
    [frontend.config :as config]))

(defn dev-setup []
  (when config/debug?
        (enable-console-print!)
        (println "Tickets4Sale development mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (defonce _init (re-frame/dispatch-sync [::events/initialize-db]))
  (dev-setup)
  (mount-root))
