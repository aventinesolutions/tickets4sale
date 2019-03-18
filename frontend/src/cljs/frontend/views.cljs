(ns frontend.views
  (:require
    [re-frame.core :as re-frame]
    [frontend.subs :as subs]))

(defn date-input
  "component for inputting the show date"
  []
  [:form
   [:label "Please enter a show date"
    [:input {:type :text :value "YYYY-MM-DD" :width 10}]
    [:input {:type :submit :value "Query ticket status"}]]])

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 "Welcome to " @name]
     [:div [date-input]]]))

