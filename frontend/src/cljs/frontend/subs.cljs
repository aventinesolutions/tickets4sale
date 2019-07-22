(ns frontend.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db _]
   (:name db)))

(re-frame/reg-sub
 ::inventory
 (fn [db _]
   (:inventory db)))
