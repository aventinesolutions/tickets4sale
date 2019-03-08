(ns tickets4sale.core
  (:gen-class)
  (:require [tickets4sale.system :as system]
            [clojure.string :as string]))

(defn -main "cli path-to-csv-file: start and use a CLI system" [& args]
  (if
    (= "cli" (string/lower-case (first args)))
    (system/init-cli-system (nth args 1))
    (println "don't know what to do")))
