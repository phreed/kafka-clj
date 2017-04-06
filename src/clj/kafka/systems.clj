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
      "vu-isis_gme_brass_immortals"
      "localhost:9092"
      {:to StringSerializer
       :from StringDeserializer}
      {:to ByteArraySerializer
       :from ByteArrayDeserializer})])


(defsystem prod-system
  [:kafka-db
    (new-kafka-server
      "vu-isis_gme_brass_immortals"
      "localhost:9092"
      {:to StringSerializer
       :from StringDeserializer}
      {:to ByteArraySerializer
       :from ByteArrayDeserializer})])
