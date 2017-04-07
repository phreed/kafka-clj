
(ns kafka.producer
  (:require [system.repl :refer [system set-init! start stop reset]]
            [system.components.kafka-server :as server]
            [clojure.pprint :as pp]))

(defn run-sample []
  (let
   [data-bytes
     {:bytes     (byte-array [(byte 1) (byte 2) (byte 3)])
      :nil       nil
      :stuff [1 2 3]
      :true      true
      :false     false
      :string    "pry bar"}
    (server/send-message (:kafka-db system) data-bytes)]))

;; (run-sample)
