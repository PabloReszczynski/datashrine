(defproject castle.fortune "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [clj-http "3.10.0"]
                 [reaver "0.1.2"]
                 [io.replikativ/datahike "0.2.0"]
                 [clj-json "0.5.3"]]

  :profiles {:dev {:dependencies [[midje/midje "1.9.8"]
                                  [nrepl "0.7.0-alpha3"]
                                  [clojure-complete "0.2.5"]]}}

  :repl-options {:init-ns castle.fortune})
