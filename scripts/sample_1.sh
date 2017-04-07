#!/bin/bash

export KAFKA_HOME=/home/kafka/kafka_2.11-0.10.2.0/

${KAFKA_HOME}bin/kafka-topics.sh \
  --zookeeper localhost:2181 \
  --list

${KAFKA_HOME}bin/kafka-topics.sh \
  --create \
  --zookeeper localhost:2181 \
  --replication-factor 1 \
  --partitions 1 \
  --topic "darpa.brass.immortals.vu.isis.gme"

${KAFKA_HOME}bin/kafka-console-producer.sh \
   --broker-list localhost:9092 \
   --topic "darpa.brass.immortals.vu.isis.gme"

${KAFKA_HOME}bin/kafka-console-consumer.sh \
   --bootstrap-server localhost:9092 \
   --from-beginning \
   --topic "darpa.brass.immortals.vu.isis.gme"
