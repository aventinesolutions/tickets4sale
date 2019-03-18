(ns frontend.views
  (:require
    [cljs-time.core :as time]
    [cljs-time.format :as formatters]
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [frontend.subs :as subs]
    [frontend.events :as events]))

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

(defn main-panel
  "main panel for the ticket status query application"
  []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 "Welcome to " @name]
     [:div [date-input]]]))

