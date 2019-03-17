(defproject tickets4sale "0.0.1-US1"
  :description "Coding Challenge for Vakantie Discounter and Otravo (Spilberg)"
  :url "https://gitlab.com/aventinesolutions/tickets4sale"
  :license
  {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
   :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [com.stuartsierra/component "0.4.0"]
                 [clojure.java-time "0.3.2"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/tools.cli "0.4.1"]
                 [aleph "0.4.6"]
                 [hiccup "1.0.5"]
                 [bidi "2.1.5"]]
  :plugins [[lein-eftest "0.5.7"]]
  :main ^:skip-aot tickets4sale.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :global-vars {*warn-on-reflection* true})
