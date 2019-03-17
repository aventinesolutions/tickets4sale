(ns tickets4sale.core
  (:gen-class)
  (:require [tickets4sale.store :as store]
            [tickets4sale.view :as view]
            [clojure.string :as string]
            [java-time :as jt]
            [clojure.tools.cli :as cli]
            [clojure.java.io :as io]))

(def cli-options
  [["-r"
    "--run cli-or-server"
    "CLI or server mode"
    :default  "cli"
    :parse-fn #(some #{%} ["cli" "server"])
    :validate [#(some #{%} ["cli" "server"])
               "Required: please indicate \"cli\" or \"server\""]]
   ["-q"
    "--query-date YYYY-MM-DD"
    "Query Date"
    :default  (jt/local-date)
    :parse-fn #(jt/local-date %)]
   ["-s"
    "--show-date YYYY-MM-DD"
    "Show Date"
    :parse-fn #(jt/local-date %)
    :validate [#(jt/local-date %) "Required: please provide a valid show date"]]
   ["-d"
    "--data /path/to/file.csv"
    "Path to CSV file with initial data"
    :default  "./test/fixtures/shows.csv"
    :validate [#(.exists (io/as-file %))
               "Please provide a valid CSV path to an existing file"]]])

(defn -main "CLI or HTTP server backend system" [& args]
  (let [options  (cli/parse-opts args cli-options)
        run-opts (:options options)]
    (cond
      (empty? args)                                 (println (:summary options))
      (not (empty? (:errors options)))              (println (:errors options) (:summary options))
      (= (:run run-opts) "cli")                     (println
                                                     (let [query-date (:query-date run-opts)
                                                           show-date  (:show-date run-opts)
                                                           file-path  (:data run-opts)
                                                           shows      (:shows (store/initialize-from-csv (:data run-opts)))]
                                                       (view/ticket-status-report query-date show-date shows)))
      :else
      (println options "Server run mode not ready. Please be patient."))))
