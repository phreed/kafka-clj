
(ns kafka.producer
  (:require [system.repl :refer [system set-init! start stop reset]]
            [system.components.kafka-server :as server]
            [clojure.pprint :as pp]))

(def data-bytes
  {:bytes     (byte-array [(byte 1) (byte 2) (byte 3)])
   :nil       nil
   :true      true
   :false     false
   :string    "junk bar"})

(server/send-message (:kafka-db system) data-bytes)

;; (pp/pprint system)
