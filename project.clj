(defproject tickets4sale "0.1.0-SNAPSHOT"
  :description "Coding Challenge for Vakantie Discounter and Otravo (Spilberg)"
  :url "https://gitlab.com/aventinesolutions/tickets4sale"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :main ^:skip-aot tickets4sale.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
