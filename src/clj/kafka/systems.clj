(ns kafka.systems
  "common variables shared between kafka producers and consumers"
  (:require
    [system.core :refer [defsystem]]
    (system.components
      [kafka-server :refer [new-kafka-server]])
    [environ.core :refer [env]]
    [cognitect.transit :as transit])
  (:import (org.apache.kafka.common.serialization
              ByteArraySerializer ByteArrayDeserializer
              StringSerializer StringDeserializer)))

;; https://techblog.roomkey.com/posts/clojure-kafka.html
;; http://kafka.apache.org/090/javadoc/index.html?overview-summary.html
;; http://blog.klipse.tech/clojure/2016/09/22/transit-clojure.html
;; https://github.com/cognitect/transit-clj

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
