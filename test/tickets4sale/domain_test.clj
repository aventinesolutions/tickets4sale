(ns tickets4sale.domain-test
  (:require [clojure.test :refer :all]
            [tickets4sale.domain :refer :all]
            [java-time :as time]))

(deftest domain-tests

  (deftest domain-constants-test
    (testing "the number of days a show always runs"
             (is (= (time/days 100) show-run-in-days)))
    (testing "venue is always moved to the smaller after days"
             (is (= 60 move-to-smaller-venue-after-days)))
    (testing "the larger venue always has seats capacity"
             (is (= 200 larger-venue-capacity-seats)))
    (testing "the smaller venue always has seats capacity"
             (is (= 100 smaller-venue-capacity-seats)))
    (testing "sales start the number of days before the show starts is always"
             (is (= (time/days 25) sales-start-days-before-opening)))
    (testing "the number of tickets sold per day for larger venues is always"
             (is (= 10 tickets-sold-per-day-bigger-venue)))
    (testing "the number of tickets sold per day for smaller venus is always"
             (is (= 5 tickets-sold-per-day-smaller-venue))))

  (let [show-opening (time/local-date "2019-01-15")]
    (deftest ticket-sales-start-test
      (testing "provides a date sales will/did start"
               (is
                (=
                 (time/local-date "2018-12-21")
                 (ticket-sales-start show-opening)))))

    (let [query-date (time/local-date "2018-12-31")]
      (deftest ticket-sales-started?-test
        (testing "true if sales have started based on query date"
                 (is
                  (true?
                   (ticket-sales-started? query-date show-opening))))
        (testing "true if sales started exeactly on query date"
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
                 (show-status (time/local-date "2019-01-03") show-opening)))))))