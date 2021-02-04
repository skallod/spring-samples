./bin/kafka-topics.sh -create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 10 --topic gal-test-topic
./bin/kafka-topics.sh -create --broker-list localhost:9092 --replication-factor 1 --partitions 10 --topic gal-test-topic
./bin/kafka-topics.sh -create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 10 --topic gal-test-topic
./bin/kafka-topics.sh -create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 10 --topic gal-compacted-topic \
--config "cleanup.policy=compact" --config "delete.retention.ms=100"  --config "segment.ms=100" --config "min.cleanable.dirty.ratio=0.01"
  > error.log 2>&1
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic gal-test-topic \
--property "parse.key=true" \
--property "key.separator=,"
docker build -t custom-kafka .
docker run --name=local_kafka -p 9092:9092 -e ZOOKEEPER_SERVERS=localhost:2181 --network="host" local-kafka
docker run --name=local_zoo -p 2181:2181 -p 2888:2888 -p 3888:3888 --network="host" eventuateio/eventuate-zookeeper:0.4.0.RELEASE
docker run --name=local_zoo -p 2181:2181 -p 2888:2888 -p 3888:3888 --network="host" zoo-local
-v /var/lib/postgres:/bitnami/postgresql