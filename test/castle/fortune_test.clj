(ns castle.fortune-test
  (:require [midje.sweet :refer :all]
            [castle.fortune :as fortune]))

(fact "it gets data from a website"
  (fortune/get-data "fortune.com") => [:html])
