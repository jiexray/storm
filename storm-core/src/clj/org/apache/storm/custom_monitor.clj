(ns org.apache.storm.custom-monitor
  (:require [clojure.tools.logging :as log]))

(defn test-log []
  (log/info "custom-monitor test-log")
  (println "custom-monitor test-log"))


