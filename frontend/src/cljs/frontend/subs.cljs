(ns frontend.subs
  (:require
    [re-frame.core :as re-frame]
    [frontend.db :as db]))

(re-frame/reg-sub
 ::name
 (fn [_]
   (:name @db/db)))

(re-frame/reg-sub
 ::report
 (fn [db query]
   (js/console.log query)))
