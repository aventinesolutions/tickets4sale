(ns tickets4sale.view-test
  (:require [clojure.test :refer :all]
            [tickets4sale.view :refer :all]
            [java-time :as jt]))

(def shows [["Cats" (jt/local-date "2018-06-01") "musical"]
            ["Comedy of Errors" (jt/local-date "2018-07-01") "comedy"]
            ["Everyman" (jt/local-date "2018-08-01") "drama"]])

(def show-list
  (ticket-status-for-shows (jt/local-date "2018-08-01") (jt/local-date "2018-08-15") shows))

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

(deftest genre-keyword-as-str-test
  (testing "converts a genre keyword into a string value"
           (is (= "offbroadway" (genre-keyword-as-str :offbroadway)))))

(deftest available-genres-test
  (testing "provides sorted list of genres"
           (is
            (=
             [:comedy :drama :musical]
             (->
              (ticket-status-for-shows (jt/local-date "2018-08-01") (jt/local-date "2018-08-15") shows)
              (available-genres))))))

(deftest filter-by-genre-test
  (testing "filters a show list by a given genre"
           (is
            (=
             [{:title             "Comedy of Errors",
               :tickets-left      100,
               :tickets-available 10,
               :status            "open for sale"}]
             (filter-by-genre :comedy show-list)))))

(deftest group-by-genre-test
  (testing "provides ticket status report grouped by genres for given query and show dates"
           (is
            (=
             [{:genre "comedy",
               :shows [{:title             "Comedy of Errors",
                        :tickets-left      100,
                        :tickets-available 10,
                        :status            "open for sale"}]}
              {:genre "drama",
               :shows [{:title             "Everyman",
                        :tickets-left      100,
                        :tickets-available 10,
                        :status            "open for sale"}]}
              {:genre "musical",
               :shows [{:title             "Cats",
                        :tickets-left      50,
                        :tickets-available 5,
                        :status            "open for sale"}]}]
             (vec (group-by-genre show-list))))))

(deftest ticket-status-report-test
  (testing "provides a JSON string of the ticket status of shows based on the query date and the given show date")
  (is
   (=
    (str "{\"inventory\":"
         "[{\"genre\":\"comedy\",\"shows\":"
         "[{\"title\":\"Comedy of Errors\",\"tickets-left\":100,\"tickets-available\":10,\"status\":\"open for sale\"}]},"
         "{\"genre\":\"drama\",\"shows\":"
         "[{\"title\":\"Everyman\",\"tickets-left\":100,\"tickets-available\":10,\"status\":\"open for sale\"}]},"
         "{\"genre\":\"musical\",\"shows\":"
         "[{\"title\":\"Cats\",\"tickets-left\":50,\"tickets-available\":5,\"status\":\"open for sale\"}]}]}")
    (ticket-status-report (jt/local-date "2018-08-01") (jt/local-date "2018-08-15") shows))))
