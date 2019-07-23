(ns frontend.events
  (:require-macros
    [cljs.core.async.macros :refer [go]])
  (:require
    [cljs.pprint :refer [pprint]]
    [ajax.core :as ajax]
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
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

; https://github.com/Day8/re-frame/blob/master/docs/Talking-To-Servers.md
(re-frame/reg-event-fx
  ::request-inventory
  (fn [{db :db} [_ show-date]]
    {:http-xhrio {:method          :get
                  :uri             (str "http://localhost:8080/ticket-status/" show-date)
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [:process-response]
                  :on-failure      [:bad-response]}
     :db         (assoc db :loading? true)}))

(re-frame/reg-event-db
  :process-response
  (fn
    [db [_ response]]
    (-> db
        (assoc :loading? false)
        (assoc :inventory (:inventory (:body response))))))

(re-frame/reg-event-db
  :bad-response
  (fn
    [db [_ response]]
    (-> db
        (assoc :loading? false)
        (assoc :inventory []))
    (log (pprint (js->clj response)))))

(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    {:name "Tickets4Sale" :inventory [] :loading? false}))

(re-frame/reg-event-db
  ::report
  (fn [db [_ show-date]]
    (fetch-inventory db show-date)))

