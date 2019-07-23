(ns frontend.views
  (:require
    [cljs.pprint :refer [pprint]]
    [clojure.string :as str]
    [cljs-time.core :as time]
    [cljs-time.format :as formatters]
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [frontend.subs :as subs]
    [frontend.events :as events]
    [frontend.db :as db]))

(defn log [& args] (apply (.-log js/console) args))

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
       (if-not (nil? (:error @state)) [:div.error (:error @state)])
       [:label
        "Show Date"
        [:input
         {:id         :show-date-input
          :auto-focus true
          :type       :text
          :value      (:value @state)
          :width      10
          :on-change  (fn [event]
                        (.preventDefault event)
                        (let [value     (-> event .-target .-value)
                              show-date (parse-date value)
                              error     (if (nil? show-date)
                                          "Please enter a valid date using format YYYY-DD-MM")]
                          (swap! state assoc
                                 :value     value
                                 :show-date show-date
                                 :error     error)))}]]
       [:input.query-status-submit
        {:type     :submit
         :value    "Query ticket inventory"
         :disabled (not (nil? (:error @state)))
         :on-click (fn [event]
                     (let [value (-> event .-target .-value)]
                       (.preventDefault event)
                       (re-frame/dispatch [::events/request-inventory (:value @state)])))}]])))

(defn show-status
  "provides the ticket sales status of a individual show"
  [show]
  (let [title  (:title show)
        status (:status show)]
    [:div.show
     [:h3.title title]
     [:div.tickets-left
      [:label "Tickets left"]
      [:span (:tickets-left show)]]
     [:div.tickets-available
      [:label "Tickets available"]
      [:span (:tickets-available show)]]
     [:div.status
      [:label "Status"]
      [:span {:class (str/replace status #" " "-")} status]]]))

(defn genre-group
  "provides a group listing of show ticket status by genre"
  [group]
  (let [genre (:genre group)]
    [:div.genre-group
     [:h2.genre genre]
     (for [show (:shows group)]
       ^{:key (str genre "-" (:title show))}
       (show-status show))]))

(defn inventory-report
  "provides the inventory of tickets with their statuses"
  []
  (let [inventory (re-frame/subscribe [::subs/inventory])
       loading? (re-frame/subscribe [::subs/loading?])]
    (fn []
      (log "inventory" (pprint @inventory))
      (if @loading? [:h3.loading "loading ..."]
                    [:div.inventory
                     [:div.genre-list
                      (for [group @inventory]
                        ^{:key (:genre group)} (genre-group group))]]))))

(defn main-panel
  "main panel for the ticket status query application"
  []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 "Welcome to " @name]
     [:div.input [date-input]]
     [:div.report [inventory-report]]]))

