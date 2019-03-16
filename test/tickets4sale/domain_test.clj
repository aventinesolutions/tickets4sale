(ns tickets4sale.domain-test
  (:require [clojure.test :refer :all]
            [tickets4sale.domain :refer :all]
            [java-time :as jt]))

(defn twentysix-day-sales-pattern
  "provide a 26 day sales pattern for given query, show and premiere dates"
  [show-date premiere-date]
  (vec
   (map
    #(let [query-date
           (jt/plus (ticket-sales-start show-date) (jt/days %))]
      (tickets-sold query-date show-date premiere-date))
    (take 25 (range)))))

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
    (deftest number-of-days-between-test
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
                   (number-of-days-between (jt/local-date "1956-07-05") show-date))))))

  (deftest ticket-sales-started-tests
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
                      query-date (jt/local-date "2818-12-12")))))))))

  (deftest in-the-past?-test
    (let [show-date (jt/local-date "1776-07-04")]
      (testing "querying a show date in the past"
               (is (true? (in-the-past? (jt/local-date "1776-07-05") show-date))))
      (testing "querying a show date in the future or present"
               (is (false? (in-the-past? (jt/local-date "1776-07-03") show-date))))))

  (let [premiere-date (jt/local-date "2017-04-06")]
    (deftest in-smaller-venue?-test
      (testing "true if, for the given show date, show performs in the smaller venue"
               (is
                (true?
                 (in-smaller-venue? (jt/local-date "2017-06-06") premiere-date))))
      (testing "false if, for the given show date, show still performs in the larger venue"
               (is
                (false?
                 (in-smaller-venue? (jt/local-date "2017-06-04") premiere-date)))))

    (deftest capacity-test
      (testing "200 if show performs in the larger venue"
               (is (= 200 (capacity (jt/local-date "2017-06-04") premiere-date))))
      (testing "100 if show performs in the smaller venue"
               (is (= 100 (capacity (jt/local-date "2017-06-06") premiere-date)))))

    (deftest sold-per-day-test
      (testing "10 if show performs in the larger venue"
               (is (= 10 (sold-per-day (jt/local-date "2017-06-04") premiere-date))))
      (testing "5 if show performs in the smaller venue"
               (is (= 5 (sold-per-day (jt/local-date "2017-06-06") premiere-date))))))

  (deftest tickets-sold-test
    (testing "tickets sold pattern for a performance in the larger venue"
             (let [sales-pattern (let [show-date     (jt/local-date "2017-06-06")
                                       premiere-date (jt/local-date "2017-05-06")]
                                   (twentysix-day-sales-pattern show-date premiere-date))]
               (is
                (=
                 [0
                  0
                  10
                  20
                  30
                  40
                  50
                  60
                  70
                  80
                  90
                  100
                  110
                  120
                  130
                  140
                  150
                  160
                  170
                  180
                  190
                  200
                  200
                  200
                  200]
                 sales-pattern))))
    (testing "tickets sold pattern for a performance in the smaller venue"
             (let [sales-pattern (let [show-date     (jt/local-date "2006-01-01")
                                       premiere-date (jt/local-date "2005-10-17")]
                                   (twentysix-day-sales-pattern show-date premiere-date))]
               (is
                (=
                 [0
                  0
                  5
                  10
                  15
                  20
                  25
                  30
                  35
                  40
                  45
                  50
                  55
                  60
                  65
                  70
                  75
                  80
                  85
                  90
                  95
                  100
                  100
                  100
                  100]
                 sales-pattern))))
    (testing "zero tickets are sold when sales have not started yet"
             (let [query-date    (jt/local-date "1998-02-01")
                   show-date     (jt/local-date "1998-06-15")
                   premiere-date show-date]
               (is (= 0 (tickets-sold query-date show-date premiere-date)))))
    (testing "assumes capacity tickets sold when query date is after the show date"
             (let [query-date    (jt/local-date "1958-09-22")
                   show-date     (jt/local-date "1958-09-21")
                   premiere-date show-date]
               (is (= 200 (tickets-sold query-date show-date premiere-date))))))

  (deftest tickets-left-test
    (testing "calculates tickets left for a show date in the bigger venue"
             (let [show-date     (jt/local-date "2017-06-06")
                   premiere-date (jt/minus show-date (jt/days 10))
                   query-date    (jt/minus show-date (jt/days 20))]
               (is (= 160 (tickets-left query-date show-date premiere-date)))))
    (testing "calculates tickets left for a show date in the smaller venue"
             (let [show-date     (jt/local-date "1902-08-31")
                   premiere-date (jt/minus show-date (jt/days 62))
                   query-date    (jt/minus show-date (jt/days 18))]
               (is (= 70 (tickets-left query-date show-date premiere-date)))))
    (testing "calculates tickets left when sales haven't started yet"
             (let [show-date     (jt/local-date "2024-04-01")
                   premiere-date show-date
                   query-date    (jt/minus show-date (jt/days 26))]
               (is (= 200 (tickets-left query-date show-date premiere-date)))))
    (testing "calculates tickets left when the query date is after the show date"
             (let [show-date     (jt/local-date "1896-12-25")
                   premiere-date show-date
                   query-date    (jt/plus show-date (jt/days 2))]
               (is (= 0 (tickets-left query-date show-date premiere-date))))))

  (deftest sold-out?-test
    (testing "all tickets are sold"
             (let [show-date     (jt/local-date "1942-11-02")
                   premiere-date (jt/minus show-date (jt/days 2))
                   query-date    (jt/minus show-date (jt/days 4))]
               (is (true? (sold-out? query-date show-date premiere-date)))))
    (testing "not all tickets are sold"
             (let [show-date     (jt/local-date "1946-07-22")
                   premiere-date (jt/minus show-date (jt/days 2))
                   query-date    (jt/minus show-date (jt/days 22))]
               (is (false? (sold-out? query-date show-date premiere-date))))))

  (deftest show-status-tests
    (testing "\"in the past \" when query date is after the show date"
             (let [show-date     (jt/local-date "1978-06-22")
                   premiere-date (jt/minus show-date (jt/days 2))
                   query-date    (jt/plus show-date (jt/days 2))]
               (is
                (=
                 "in the past"
                 (show-status query-date show-date premiere-date)))))
    (testing "\"sale not started \" when query date is before sale start date for performance"
             (let [show-date     (jt/local-date "1978-06-14")
                   premiere-date (jt/minus show-date (jt/days 2))
                   query-date    (jt/minus show-date (jt/days 28))]
               (is
                (=
                 "sale not started"
                 (show-status query-date show-date premiere-date)))))
    (testing "\"sold out \" when all tickets are sold based on query date"
             (let [show-date     (jt/local-date "1978-06-30")
                   premiere-date (jt/minus show-date (jt/days 4))
                   query-date    (jt/minus show-date (jt/days 4))]
               (is
                (=
                 "sold out"
                 (show-status query-date show-date premiere-date)))))
    (testing "\"open for sale \" when otherwise tickets are available based on query date"
             (let [show-date     (jt/local-date "1978-06-05")
                   premiere-date (jt/minus show-date (jt/days 6))
                   query-date    (jt/minus show-date (jt/days 16))]
               (is
                (=
                 "open for sale"
                 (show-status query-date show-date premiere-date)))))))





