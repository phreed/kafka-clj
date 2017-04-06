
(ns kafka.consumer
  (:require [system.repl :refer [system set-init! start stop reset]]
            [system.components.kafka-server :as server]
            [clojure.pprint :as pp]))

(future 
  (server/receive-messages (:kafka-db system) 
     (fn [item] (println item))))
