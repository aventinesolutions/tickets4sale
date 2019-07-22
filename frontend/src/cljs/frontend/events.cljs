(ns frontend.events
  (:require-macros
    [cljs.core.async.macros :refer [go]])
  (:require
    [cljs.pprint :refer [pprint]]
    [re-frame.core :as re-frame]
    [cljs-http.client :as http]
    [cljs.core.async :refer [<!]]))

(defn log [& args] (apply (.-log js/console) args))

(defn- fetch-inventory
  "fetch the ticket status inventory report from the backend"
  [db show-date]
  (go
    (let [response (<!
                     (http/get (str "http://localhost:8080/ticket-status/" show-date)
                               {:with-credentials? false}))
          inventory (:inventory (:body response))]
      (log (pprint inventory))
      (assoc db :inventory inventory))))

(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    {:name "Tickets4Sale" :inventory []}))

(re-frame/reg-event-db
  ::report
  (fn [db [_ show-date]]
    (fetch-inventory db show-date)))

