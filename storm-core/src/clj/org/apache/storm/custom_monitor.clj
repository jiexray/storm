(ns org.apache.storm.custom-monitor
  (:require [clojure.tools.logging :as log])
  (:use [org.apache.storm config]))

(defn test-log []
  (log/info "custom-monitor test-log")
  (println "custom-monitor test-log"))


(defn test-conf [conf]
  (if (conf CUSTOM-MONITOR-ENABLE)
    (println "enable custom-monitor")
    (println "disable custom-monitor")))


