(ns system.components.kafka-server
  (:require [com.stuartsierra.component :as component]
            [cognitect.transit :as transit]
            [clojure.pprint :as pp]
            [clojure.tools.logging :as log])
  (:import (java.io
            ByteArrayOutputStream
            ByteArrayInputStream)
           (org.apache.kafka.common
            TopicPartition
            KafkaException)
           (org.apache.kafka.clients.producer
            KafkaProducer
            ProducerRecord)
           (org.apache.kafka.common
            TopicPartition)
           (org.apache.kafka.common.errors
            WakeupException)
           (org.apache.kafka.clients.consumer
            KafkaConsumer
            OffsetAndMetadata)))

(def wire-encoding :json)

(defrecord Kafka [main-topic bootstrap-servers value-serializer key-serializer]
  component/Lifecycle
  (start [component]
    (let [producer-cfg
          {"bootstrap.servers" bootstrap-servers
           "value.serializer" (:to value-serializer)
           "key.serializer" (:to key-serializer)}
          producer (KafkaProducer. producer-cfg)

          consumer-cfg
          {"bootstrap.servers" bootstrap-servers
           "group.id" "gme-consumer"
           "auto.offset.reset" "earliest"
           "enable.auto.commit" "false"
           "value.deserializer" (:from  value-serializer)
           "key.deserializer" (:from  key-serializer)}
          consumer (doto
                    (KafkaConsumer. consumer-cfg)
                    (.subscribe [main-topic]))]

      (assoc component
             :producer producer
             :consumer consumer
             :consumer-cfg consumer-cfg
             :min-poll-size 2
             :min-batch-size 10)))

  (stop [component]
    (try (.close (:producer component))
         (catch KafkaException ex nil))
    (try (.close (:consumer component))
         (catch KafkaException ex nil))
    component))

(defn new-kafka-server
  "provide a connection to a kafka server"
  [topic servers value key]
  (map->Kafka {:main-topic topic
               :bootstrap-servers servers
               :value-serializer value
               :key-serializer key}))

(defn send-message
  "Send the given record asynchronously and return a future
   which will eventually contain the response information."
  [component record]
  (log/info "sending" (with-out-str (pp/pprint record)))
  (let [buffer (ByteArrayOutputStream. 4096)
        writer (transit/writer buffer wire-encoding)]
    (transit/write writer record)
    (let [record-bytes (.toByteArray buffer)
          prod-record (ProducerRecord. (:main-topic component) record-bytes)
          res (.send (:producer component) prod-record)]
      (log/info "result" res))))

(defn metrics
  "Return a map of metrics maintained by the producer"
  [component]
  (.metrics (:producer component)))

(defn- process-batch
  [batch action]
  (loop [record (first batch)
         remainder (rest batch)]
    (let [topic (.topic record)
          key (.key record)
          value (.value record)
          buffer (ByteArrayInputStream. value)
          reader (transit/reader buffer wire-encoding)
          item (transit/read reader)]
       (action item))
    (when (seq remainder)
      (Thread/sleep 1000)
      (recur (first remainder) (rest remainder)))))

(defn- consume-lazy [consumer min-poll-size]
  (lazy-seq
   (concat (.poll consumer min-poll-size)
           (consume-lazy consumer min-poll-size))))

(defn- commit-tuple [r]
  [(TopicPartition. (.topic r) (.partition r))
   (OffsetAndMetadata. (+ 1 (.offset r)))])

(defn- calculate-offsets [buffer]
  (reduce
   (fn [acc y] (conj acc (commit-tuple y)))
   {} buffer))

(defn receive-messages
  "receive any incoming messages"
  [component action]
  (let [consumer (:consumer component)
        min-batch-size (:min-batch-size component)
        min-poll-size (:min-poll-size component)]
    (try
      (loop [lc (consume-lazy consumer min-poll-size)]
        (let [batch (take min-batch-size lc)]
          (process-batch batch action)
          (.commitSync consumer (calculate-offsets batch))
          (recur (drop min-batch-size lc))))
      (catch WakeupException ex ()))))
