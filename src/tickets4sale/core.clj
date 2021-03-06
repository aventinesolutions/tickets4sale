(ns tickets4sale.core
  (:gen-class)
  (:require [tickets4sale.store :as store]
            [tickets4sale.view :as view]
            [tickets4sale.system :refer [init-system start!]]
            [clojure.string :as string]
            [java-time :refer [local-date]]
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
    :default  (local-date)
    :parse-fn #(local-date %)]
   ["-s"
    "--show-date YYYY-MM-DD"
    "Show Date"
    :parse-fn #(local-date %)
    :validate [#(local-date %) "Required: please provide a valid show date"]]
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
      (nil? (:show-date run-opts))                  (println "Show date is required" \newline (:summary options))
      (= (:run run-opts) "cli")                     (println
                                                     (let [query-date (:query-date run-opts)
                                                           show-date  (:show-date run-opts)
                                                           file-path  (:data run-opts)
                                                           shows      (:shows (store/initialize-from-csv (:data run-opts)))]
                                                       (view/ticket-status-report query-date show-date shows)))
      :else
      ; run HTTP server via system component
      (do
        (store/initialize-from-csv (:data run-opts))
        (swap! store/store assoc :query-date (:query-date run-opts) :show-date (:show-date run-opts))
        (init-system)
        (start!)))))
