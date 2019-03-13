(ns tickets4sale.domain-test
  (:require [clojure.test :refer :all]
            [tickets4sale.domain :refer :all]
            [java-time :as jt]))

(deftest domain-tests

  (deftest domain-constants-test
    (testing "the number of days a show always runs"
             (is (= (jt/days 100) show-run-in-days)))
    (testing "venue is always moved to the smaller after days"
             (is (= (jt/days 60) move-to-smaller-venue-after-days)))
    (testing "the larger venue always has seats capacity"
             (is (= 200 larger-venue-capacity-seats)))
    (testing "the smaller venue always has seats capacity"
             (is (= 100 smaller-venue-capacity-seats)))
    (testing "sales start the number of days before the performance is always"
             (is (= (jt/days 25) sales-start-days-before-show)))
    (testing "the number of tickets sold per day for larger venues is always"
             (is (= 10 tickets-sold-per-day-bigger-venue)))
    (testing "the number of tickets sold per day for smaller venus is always"
             (is (= 5 tickets-sold-per-day-smaller-venue))))

  (let [show-date (jt/local-date "1956-07-05")]
    (deftest number-of-days-between-test)
    (testing "a calendar interval representing positive days"
             (is
              (= 82
                 (number-of-days-between (jt/local-date "1956-04-14") show-date))))
    (testing "a calendar interval representing negative days"
             (is
              (= -79
                 (number-of-days-between (jt/local-date "1956-09-22") show-date))))
    (testing "a zero canlendar interval on the same day"
             (is
              (= 0
                 (number-of-days-between (jt/local-date "1956-07-05") show-date)))))

  (let [show-date (jt/local-date "2019-01-15")]
    (deftest ticket-sales-start-test
      (testing "provides a date sales will/did start"
               (is
                (=
                 (jt/local-date "2018-12-21")
                 (ticket-sales-start show-date)))))

    (let [query-date (jt/local-date "2018-12-31")]
      (deftest ticket-sales-started?-test
        (testing "true if sales have started based on query date and performance date"
                 (is
                  (true?
                   (ticket-sales-started? query-date show-date))))
        (testing "true if sales started exactly on query date"
                 (is
                  (true?
                   (ticket-sales-started?
                    (jt/local-date "2018-12-21") show-date))))
        (testing "false if sales have not yet started base on query date"
                 (is
                  (false?
                   (ticket-sales-started?
                    query-date (jt/local-date "2818-12-12"))))))))

  (let [show-date (jt/local-date "2019-02-02")]
    (deftest show-status-tests
      (testing "\"sale not started \" when query date is before sale start date for performance"
               (is
                (=
                 "sale not started"
                 (show-status (jt/local-date "2019-01-03") show-date)))))))