(ns frontend.events
  (:require
    [re-frame.core :as re-frame]
    [frontend.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   (db/initialize-db)))

(re-frame/reg-event-db
 ::report
 (fn [_ [_ show-date]]
   (db/fetch-inventory show-date)))
