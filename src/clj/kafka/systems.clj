(ns kafka.systems
  "common variables shared between kafka producers and consumers"
  (:require
    [system.core :refer [defsystem]]
    (system.components
      [kafka-server :refer [new-kafka-server]]))
  (:import (org.apache.kafka.common.serialization
              ByteArraySerializer ByteArrayDeserializer
              StringSerializer StringDeserializer)))

(defsystem dev-system
  [:kafka-db
    (new-kafka-server
      "darpa.brass.immortals.vu.isis.gme"
      "localhost:9092"
      {:to ByteArraySerializer
       :from ByteArrayDeserializer}
      {:to ByteArraySerializer
       :from ByteArrayDeserializer})])


(defsystem prod-system
  [:kafka-db
    (new-kafka-server
      "darpa.brass.immortals.vu.isis.gme"
      "localhost:9092"
      {:to ByteArraySerializer
       :from ByteArrayDeserializer}
      {:to ByteArraySerializer
       :from ByteArrayDeserializer})])
