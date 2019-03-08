(ns tickets4sale.core
  (:gen-class)
  (:require [tickets4sale.store :as store]
            [clojure.string :as string]))

(defn -main "cli path-to-csv-file: start and use a CLI system" [& args]
  (cond
    (empty? args)
    (println "please indicate you want to use 'cli' or 'server'")
    (= "cli" (string/lower-case (first args)))
    (do (store/initialize-from-csv (nth args 1))
      (println (:shows @store/store)))
    :else (println "don't know what to do" args "...?")))
