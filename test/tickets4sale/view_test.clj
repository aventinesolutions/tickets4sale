(ns tickets4sale.view-test
  (:require [clojure.test :refer :all]
            [tickets4sale.view :refer :all]
            [java-time :as jt]))

(def shows [["Cats" (jt/local-date "2018-06-01") "musical"]
            ["Comedy of Errors" (jt/local-date "2018-07-01") "comedy"]
            ["Everyman" (jt/local-date "2018-08-01") "drama"]])

(deftest ticket-status-for-show-test
  (testing "querying a show before sales have started"
           (is
            (=
             [{:title             "Cats"
               :genre             "musical"
               :tickets-left      200
               :tickets-available 0
               :status            "sale not started"}
              {:title             "Comedy of Errors"
               :genre             "comedy"
               :tickets-left      200
               :tickets-available 0
               :status            "sale not started"}
              {:title             "Everyman"
               :genre             "drama"
               :tickets-left      200
               :tickets-available 0
               :status            "sale not started"}]
             (vec
              (map
               #(ticket-status-for-show (jt/local-date "2018-01-01") (jt/local-date "2018-07-01") %)
               shows)))))
  (testing "querying a show after sales have started"
           (is
            (=
             [{:title             "Cats"
               :genre             "musical"
               :tickets-left      50
               :tickets-available 5
               :status            "open for sale"}
              {:title             "Comedy of Errors"
               :genre             "comedy"
               :tickets-left      100
               :tickets-available 10
               :status            "open for sale"}
              {:title             "Everyman"
               :genre             "drama"
               :tickets-left      100
               :tickets-available 10
               :status            "open for sale"}]
             (vec
              (map
               #(ticket-status-for-show (jt/local-date "2018-08-01") (jt/local-date "2018-08-15") %)
               shows))))))

(deftest ticket-status-for-shows-test
  (testing "querying shows before sales have started"
           (is
            (=
             [{:title             "Cats"
               :genre             "musical"
               :tickets-left      200
               :tickets-available 0
               :status            "sale not started"}
              {:title             "Comedy of Errors"
               :genre             "comedy"
               :tickets-left      200
               :tickets-available 0
               :status            "sale not started"}
              {:title             "Everyman"
               :genre             "drama"
               :tickets-left      200
               :tickets-available 0
               :status            "sale not started"}]
             (ticket-status-for-shows (jt/local-date "2018-01-01") (jt/local-date "2018-07-01") shows))))
  (testing "querying a show after sales have started"
           (is
            (=
             [{:title             "Cats"
               :genre             "musical"
               :tickets-left      50
               :tickets-available 5
               :status            "open for sale"}
              {:title             "Comedy of Errors"
               :genre             "comedy"
               :tickets-left      100
               :tickets-available 10
               :status            "open for sale"}
              {:title             "Everyman"
               :genre             "drama"
               :tickets-left      100
               :tickets-available 10
               :status            "open for sale"}]
             (ticket-status-for-shows (jt/local-date "2018-08-01") (jt/local-date "2018-08-15") shows)))))

(deftest available-genres-test
  (testing "provides sorted list of genres"
           (is
            (=
             [:comedy :drama :musical]
             (->
              (ticket-status-for-shows (jt/local-date "2018-08-01") (jt/local-date "2018-08-15") shows)
              (available-genres))))))
