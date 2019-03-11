(ns tickets4sale.domain-test
  (:require [clojure.test :refer :all]
            [tickets4sale.domain :refer :all]))

(deftest domain

  (deftest domain-constants
    (testing "the number of days a show always runs"
             (is (= 100 show-run-in-days)))
    (testing "venue is always moved to the smaller after days"
             (is (= 60 move-to-smaller-venue-after-days)))
    (testing "the larger venue always has seats capacity"
             (is (= 200 larger-venue-capacity-seats)))
    (testing "the smaller venue always has seats capacity"
             (is (= 100 smaller-venue-capacity-seats)))
    (testing "sales start the number of days before the show starts is always"
             (is (= 25 sales-start-days-before-opening)))
    (testing "the number of tickets sold per day for larger venues is always"
             (is (= 10 tickets-sold-per-day-bigger-venue)))
    (testing "the number of tickets sold per day for smaller venus is always"
             (is (= 5 tickets-sold-per-day-smaller-venue))))

  (deftest ticket-sales-start-test
    (testing "provides a date if sales starts" (is (true? false)))
    (testing "nil if sales have not yet started" (is (true? false))))

  (deftest ticket-sales-started?-test
    (testing "true if sales have started" (is (true? false)))
    (testing "fals if sales have not yet started" (is (true? false)))))