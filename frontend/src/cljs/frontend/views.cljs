(ns frontend.views
  (:require
    [cljs-time.core :as time]
    [cljs-time.format :as formatters]
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [frontend.subs :as subs]
    [frontend.events :as events]
    [frontend.db :as db]))

(def date-format (formatters/formatters :date))

(defn parse-date
  "parse date from string using the format YYYY-MM-DD, return nil if format is invalid"
  [value]
  (try (formatters/parse date-format value)
    (catch js/Object e nil)))

(defn date-input
  "component for inputting the show date"
  []
  (let [state (reagent/atom {:value "2018-08-01"})]
    (fn []
      [:form
       {:id :show-date-input-form}
       [:div {:id :error} (:error @state)]
       [:label
        "Show Date"
        [:input
         {:id        :show-date-input
          :type      :text
          :value     (:value @state)
          :width     10
          :on-change (fn [event]
                       (.preventDefault event)
                       (let [value     (-> event .-target .-value)
                             show-date (parse-date value)
                             error     (if (nil? show-date)
                                         "Please enter a valid date using format YYYY-DD-MM")]
                         (swap! state assoc
                                :value     value
                                :show-date show-date
                                :error     error)))}]]
       [:input
        {:id       :query-status-submit
         :type     :submit
         :value    "Query ticket status"
         :disabled (not (nil? (:error @state)))
         :on-click (fn [event]
                     (let [value (-> event .-target .-value)]
                       (.preventDefault event)
                       (swap! state assoc :submitted true)
                       (re-frame/dispatch [::events/report (:value @state)])))}]
       [:div (pr-str @state)]])))

(defn show-status
  "provides the ticket sales status of a show"
  [show]
  [:div.show
   {:key (:title show)}
   [:h3.title (:title show)]
   [:div.tickets-left
    [:label "Tickets left"]
    [:span (:tickets-left show)]]
   [:div.tickets-available
    [:label "Tickets available"]
    [:span (:tickets-available show)]]
   [:div.status
    [:label "Status"]
    [:span (:status show)]]])

(defn genre-group
  "provides a group of show ticket status by genre"
  [group]
  (let [genre (:genre group)]
    [:div.genre-group
     [:h2.genre {:key genre} genre]
     (map #(let [show %] (show-status show)) (:shows group))]))

(defn inventory-report
  "provides the inventory of tickets with their statuses"
  []
  (let [state db/db]
    (fn []
      [:div.inventory
       [:div.genre-list
        (map #(let [group %] (genre-group group)) (:inventory @state))]])))

(defn main-panel
  "main panel for the ticket status query application"
  []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 "Welcome to " @name]
     [:div.input [date-input]]
     [:div.report [inventory-report]]]))

