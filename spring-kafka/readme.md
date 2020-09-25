./bin/kafka-topics.sh -create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 10 --topic gal-test-topic
./bin/kafka-topics.sh -create --broker-list localhost:9092 --replication-factor 1 --partitions 10 --topic gal-test-topic
./bin/kafka-topics.sh -create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 10 --topic gal-test-topic
./bin/kafka-topics.sh -create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 10 --topic gal-compacted-topic \
--config "cleanup.policy=compact" --config "delete.retention.ms=100"  --config "segment.ms=100" --config "min.cleanable.dirty.ratio=0.01"
  > error.log 2>&1
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic gal-test-topic \
--property "parse.key=true" \
--property "key.separator=,"