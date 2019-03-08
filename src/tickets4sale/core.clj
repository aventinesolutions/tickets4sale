(ns tickets4sale.core
  (:gen-class)
  (:require [tickets4sale.system :as system]
            [clojure.string :as string]))

(defn -main "cli path-to-csv-file: start and use a CLI system" [& args]
  (cond
    (empty? args)
    (println "please indicate you want to use 'cli' or 'server'")
    (= "cli" (string/lower-case (first args)))
    (doall (system/init-cli-system (nth args 1)) (system/start-cli!))
    :else (println "don't know what to do" args "...?")))
