(ns tickets4sale.domain-test
  (:require [clojure.test :refer :all]
            [tickets4sale.domain :refer :all]
            [java-time :as time]))

(deftest domain-tests

  (deftest domain-constants-test
    (testing "the number of days a show always runs"
             (is (= (time/days 100) show-run-in-days)))
    (testing "venue is always moved to the smaller after days"
             (is (= (time/days 60) move-to-smaller-venue-after-days)))
    (testing "the larger venue always has seats capacity"
             (is (= 200 larger-venue-capacity-seats)))
    (testing "the smaller venue always has seats capacity"
             (is (= 100 smaller-venue-capacity-seats)))
    (testing "sales start the number of days before the show starts is always"
             (is (= (time/days 25) sales-start-days-before-opening)))
    (testing "the number of tickets sold per day for larger venues is always"
             (is (= 10 tickets-sold-per-day-bigger-venue)))
    (testing "the number of tickets sold per day for smaller venus is always"
             (is (= 5 tickets-sold-per-day-smaller-venue)))
    (testing "the total inventory of tickets that could be sold for bigger venue before sales start"
             (is (= 12000 ticket-inventory-bigger-venue-before-start))))

  (let [show-opening (time/local-date "1956-07-05")]
   (deftest number-of-days-between-test)
   (testing "a calendar interval representing positive days"
            (is
             (= 82
                (number-of-days-between (time/local-date "1956-04-14") show-opening))))
   (testing "a calendar interval representing negative days"
            (is
             (= -79
                (number-of-days-between (time/local-date "1956-09-22") show-opening))))
   (testing "a zero canlendar interval on the same day"
            (is
             (= 0
                (number-of-days-between (time/local-date "1956-07-05") show-opening)))))

  (let [show-opening (time/local-date "2019-01-15")]
    (deftest ticket-sales-start-test
      (testing "provides a date sales will/did start"
               (is
                (=
                 (time/local-date "2018-12-21")
                 (ticket-sales-start show-opening)))))

    (let [query-date (time/local-date "2018-12-31")]
      (deftest ticket-sales-started?-test
        (testing "true if sales have started based on query date after the show opening"
                 (is
                  (true?
                   (ticket-sales-started? query-date show-opening))))
        (testing "true if sales started exactly on query date"
                 (is
                  (true?
                   (ticket-sales-started?
                    (time/local-date "2018-12-21") show-opening))))
        (testing "false if sales have not yet started base on query date"
                 (is
                  (false?
                   (ticket-sales-started?
                    query-date (time/local-date "2818-12-12"))))))))

  (let [show-opening (time/local-date "2019-02-02")]
    (deftest show-status-tests
      (testing "\"sale not started \" when query date is before sale start date"
               (is
                (=
                 "sale not started"
                 (show-status (time/local-date "2019-01-03") show-opening))))))

  (let [show-opening (time/local-date "2011-06-15")]
    (deftest tickets-left-bigger-venue-test
      (testing "sale not startued, all tickets available for bigger venue"
               (is
                (= 12000
                   (tickets-left-bigger-venue (time/local-date "2011-01-01") show-opening))))
      (testing "sale just startued, all bigger venue tickets available"
               (is
                (= 12000
                   (tickets-left-bigger-venue (time/local-date "2011-05-20") show-opening))))
      (testing "some tickets left for the bigger venue"
               (is
                (= 11860
                   (tickets-left-bigger-venue (time/local-date "2011-06-01") show-opening))))
      (testing "all tickets sold-out for the bigger venue"
               (is
                (= 0
                   (tickets-left-bigger-venue (time/local-date "2011-07-22") show-opening)))))))