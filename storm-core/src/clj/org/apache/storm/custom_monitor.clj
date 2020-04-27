(ns org.apache.storm.custom-monitor
  (:require [clojure.tools.logging :as log])
  (:use [org.apache.storm config util timer]))

(defn test-log []
  (log/info "custom-monitor test-log")
  (println "custom-monitor test-log"))


(defn test-conf [conf]
  (if (conf CUSTOM-MONITOR-ENABLE)
    (println "enable custom-monitor")
    (println "disable custom-monitor")))


(defn mk-halting-timer [timer-name]
  (mk-timer :kill-fn (fn [t]
                       (log/error t "Error when processing event")
                       (exit-process! 20 "Error when processing an event")
                       )
            :timer-name timer-name))


(defn mk-custom-monitor-data [conf worker]
  (let [enabled? (conf CUSTOM-MONITOR-ENABLE)
        ]
    (recursive-map
      :worker worker
      :enabled? enabled?
      :refresh-monitor-timer  (mk-halting-timer "refresh-monitor-timer"))))

(defn mk-refresh-monitor [custom-monitor]
  (let [
        worker-transfer-queue (:transfer-queue (:worker custom-monitor))
        queue-metrics (.getMetrics worker-transfer-queue)
        queue-states (.getState queue-metrics)]
    (fn this
      ([]
       (this (fn [& ignored] (schedule (:refresh-monitor-timer custom-monitor) 0 this))))
      ([callback]
       (log/info "refresh-monitor")
       ; read transfer-queue in worker
       (doseq [[key val] queue-states]
         (log/info (str key ":" val)))
       ))))

(defn mk-custom-monitor [conf worker]
  (let [custom-monitor (mk-custom-monitor-data conf worker)
        refresh-monitor (mk-refresh-monitor custom-monitor)]
    (log/info "start refresh-monitor-timer with schedule-recurring")
    (schedule-recurring (:refresh-monitor-timer custom-monitor) 0 (conf CUSTOM-MONITOR-POLL-SECS) refresh-monitor)
    ))


