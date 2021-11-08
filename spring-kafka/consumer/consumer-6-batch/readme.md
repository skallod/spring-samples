=== 2.5.4 === logs

2021-11-02 10:40:25.315  INFO 26196 --- [ consumer-0-C-1] o.a.k.clients.consumer.KafkaConsumer     : [Consumer clientId=consumer-1, groupId=test_galuzin_consumer] Seeking to offset 4 for partition test_galuzin_topic-0
2021-11-02 10:40:25.315 ERROR 26196 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Error handler threw an exception

org.springframework.kafka.KafkaException: Seek to current after exception; nested exception is org.springframework.kafka.listener.ListenerExecutionFailedException: Listener failed; nested exception is java.lang.IllegalStateException: db connection fail
	at org.springframework.kafka.listener.SeekToCurrentBatchErrorHandler.handle(SeekToCurrentBatchErrorHandler.java:92) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.RecoveringBatchErrorHandler.handle(RecoveringBatchErrorHandler.java:112) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.ContainerAwareBatchErrorHandler.handle(ContainerAwareBatchErrorHandler.java:56) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchErrorHandler(KafkaMessageListenerContainer.java:1624) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchListener(KafkaMessageListenerContainer.java:1498) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchListener(KafkaMessageListenerContainer.java:1378) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeListener(KafkaMessageListenerContainer.java:1361) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollAndInvoke(KafkaMessageListenerContainer.java:1080) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.run(KafkaMessageListenerContainer.java:988) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.run$$$capture(FutureTask.java:264) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java) ~[na:na]
	at java.base/java.lang.Thread.run(Thread.java:832) ~[na:na]
Caused by: org.springframework.kafka.listener.ListenerExecutionFailedException: Listener failed; nested exception is java.lang.IllegalStateException: db connection fail
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.decorateException(KafkaMessageListenerContainer.java:1926) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	... 10 common frames omitted
Caused by: java.lang.IllegalStateException: db connection fail
	at ru.galuzin.consumer.conf.ConsumerConfig$1.onMessage(ConsumerConfig.java:68) ~[classes/:na]
	at ru.galuzin.consumer.conf.ConsumerConfig$1.onMessage(ConsumerConfig.java:58) ~[classes/:na]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchOnMessage(KafkaMessageListenerContainer.java:1607) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchOnMessageWithRecordsOrList(KafkaMessageListenerContainer.java:1592) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchOnMessage(KafkaMessageListenerContainer.java:1550) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchListener(KafkaMessageListenerContainer.java:1485) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	... 8 common frames omitted

2021-11-02 10:40:25.315 DEBUG 26196 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Commit list: {}
2021-11-02 10:40:26.320 DEBUG 26196 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Received: 1 records
2021-11-02 10:40:26.320  INFO 26196 --- [ consumer-0-C-1] ru.galuzin.consumer.conf.ConsumerConfig  : batch list size 1, test_galuzin_topic, 0, 4, 123
2021-11-02 10:40:26.320 DEBUG 26196 --- [ consumer-0-C-1] o.s.k.l.RecoveringBatchErrorHandler      : Expected a BatchListenerFailedException; re-seeking batch

=== 2.5.5 ===

2021-11-02 10:45:02.780  INFO 24880 --- [ consumer-0-C-1] o.a.k.clients.consumer.KafkaConsumer     : [Consumer clientId=consumer-1, groupId=test_galuzin_consumer] Seeking to offset 4 for partition test_galuzin_topic-0
2021-11-02 10:45:02.780 ERROR 24880 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Error handler threw an exception

org.springframework.kafka.KafkaException: Seek to current after exception; nested exception is org.springframework.kafka.listener.ListenerExecutionFailedException: Listener failed; nested exception is java.lang.IllegalStateException: db connection fail
	at org.springframework.kafka.listener.SeekToCurrentBatchErrorHandler.handle(SeekToCurrentBatchErrorHandler.java:92) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.RecoveringBatchErrorHandler.handle(RecoveringBatchErrorHandler.java:112) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.ContainerAwareBatchErrorHandler.handle(ContainerAwareBatchErrorHandler.java:56) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchErrorHandler(KafkaMessageListenerContainer.java:1626) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchListener(KafkaMessageListenerContainer.java:1500) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchListener(KafkaMessageListenerContainer.java:1380) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeListener(KafkaMessageListenerContainer.java:1363) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollAndInvoke(KafkaMessageListenerContainer.java:1082) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.run(KafkaMessageListenerContainer.java:990) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.run$$$capture(FutureTask.java:264) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java) ~[na:na]
	at java.base/java.lang.Thread.run(Thread.java:832) ~[na:na]
Caused by: org.springframework.kafka.listener.ListenerExecutionFailedException: Listener failed; nested exception is java.lang.IllegalStateException: db connection fail
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.decorateException(KafkaMessageListenerContainer.java:1928) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	... 10 common frames omitted
Caused by: java.lang.IllegalStateException: db connection fail
	at ru.galuzin.consumer.conf.ConsumerConfig$1.onMessage(ConsumerConfig.java:68) ~[classes/:na]
	at ru.galuzin.consumer.conf.ConsumerConfig$1.onMessage(ConsumerConfig.java:58) ~[classes/:na]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchOnMessage(KafkaMessageListenerContainer.java:1609) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchOnMessageWithRecordsOrList(KafkaMessageListenerContainer.java:1594) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchOnMessage(KafkaMessageListenerContainer.java:1552) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchListener(KafkaMessageListenerContainer.java:1487) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	... 8 common frames omitted

2021-11-02 10:45:02.780 DEBUG 24880 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Commit list: {}
2021-11-02 10:45:02.830 DEBUG 24880 --- [ consumer-1-C-1] essageListenerContainer$ListenerConsumer : Received: 0 records
2021-11-02 10:45:02.830 DEBUG 24880 --- [ consumer-1-C-1] essageListenerContainer$ListenerConsumer : Commit list: {}
2021-11-02 10:45:03.787 DEBUG 24880 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Received: 1 records
2021-11-02 10:45:03.787  INFO 24880 --- [ consumer-0-C-1] ru.galuzin.consumer.conf.ConsumerConfig  : batch list size 1, test_galuzin_topic, 0, 4, 123
2021-11-02 10:45:03.788 DEBUG 24880 --- [ consumer-0-C-1] o.s.k.l.RecoveringBatchErrorHandler      : Expected a BatchListenerFailedException; re-seeking batch

В плане тайминга опроса не отличается , непонятно как backoff работает
  не засетил errorhandler

=== 2.5.5 good === 

2021-11-08 11:56:35.659 DEBUG 12564 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Commit list: {}
polling compare and set
2021-11-08 11:56:36.165 DEBUG 12564 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Received: 1 records
2021-11-08 11:56:36.166  INFO 12564 --- [ consumer-0-C-1] ru.galuzin.consumer.conf.ConsumerConfig  : batch list size 1, test_galuzin_topic, 0, 51, 1 3
2021-11-08 11:56:36.166  INFO 12564 --- [ consumer-0-C-1] o.a.k.clients.consumer.KafkaConsumer     : [Consumer clientId=consumer-1, groupId=test_galuzin_consumer] Seeking to offset 51 for partition test_galuzin_topic-0
2021-11-08 11:56:36.877 DEBUG 12564 --- [ consumer-1-C-1] essageListenerContainer$ListenerConsumer : Received: 0 records
poll and log
2021-11-08 11:56:36.883 DEBUG 12564 --- [ consumer-1-C-1] essageListenerContainer$ListenerConsumer : Commit list: {}
polling compare and set
seek exception
2021-11-08 11:56:39.178 ERROR 12564 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Error handler threw an exception

org.springframework.kafka.KafkaException: Seek to current after exception; nested exception is org.springframework.kafka.listener.ListenerExecutionFailedException: Listener failed; nested exception is java.lang.IllegalStateException: db connection fail
	at org.springframework.kafka.listener.SeekToCurrentBatchErrorHandler.handle(SeekToCurrentBatchErrorHandler.java:92) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.ContainerAwareBatchErrorHandler.handle(ContainerAwareBatchErrorHandler.java:56) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchErrorHandler(KafkaMessageListenerContainer.java:1626) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchListener(KafkaMessageListenerContainer.java:1500) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchListener(KafkaMessageListenerContainer.java:1380) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeListener(KafkaMessageListenerContainer.java:1363) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollAndInvoke(KafkaMessageListenerContainer.java:1082) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.run(KafkaMessageListenerContainer.java:990) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.run$$$capture(FutureTask.java:264) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java) ~[na:na]
	at java.base/java.lang.Thread.run(Thread.java:832) ~[na:na]
Caused by: org.springframework.kafka.listener.ListenerExecutionFailedException: Listener failed; nested exception is java.lang.IllegalStateException: db connection fail
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.decorateException(KafkaMessageListenerContainer.java:1928) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	... 10 common frames omitted
Caused by: java.lang.IllegalStateException: db connection fail
	at ru.galuzin.consumer.conf.ConsumerConfig$1.onMessage(ConsumerConfig.java:76) ~[classes/:na]
	at ru.galuzin.consumer.conf.ConsumerConfig$1.onMessage(ConsumerConfig.java:65) ~[classes/:na]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchOnMessage(KafkaMessageListenerContainer.java:1609) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchOnMessageWithRecordsOrList(KafkaMessageListenerContainer.java:1594) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchOnMessage(KafkaMessageListenerContainer.java:1552) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchListener(KafkaMessageListenerContainer.java:1487) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	... 8 common frames omitted

poll and log
2021-11-08 11:56:39.187 DEBUG 12564 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Commit list: {}
polling compare and set
2021-11-08 11:56:39.705 DEBUG 12564 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Received: 1 records
2021-11-08 11:56:39.705  INFO 12564 --- [ consumer-0-C-1] ru.galuzin.consumer.conf.ConsumerConfig  : batch list size 1, test_galuzin_topic, 0, 51, 1 4
2021-11-08 11:56:39.705  INFO 12564 --- [ consumer-0-C-1] o.a.k.clients.consumer.KafkaConsumer     : [Consumer clientId=consumer-1, groupId=test_galuzin_consumer] Seeking to offset 51 for partition test_galuzin_topic-0
2021-11-08 11:56:41.889 DEBUG 12564 --- [ consumer-1-C-1] essageListenerContainer$ListenerConsumer : Received: 0 records
poll and log
2021-11-08 11:56:41.894 DEBUG 12564 --- [ consumer-1-C-1] essageListenerContainer$ListenerConsumer : Commit list: {}
polling compare and set
seek exception
2021-11-08 11:56:42.717 ERROR 12564 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Error handler threw an exception

org.springframework.kafka.KafkaException: Seek to current after exception; nested exception is org.springframework.kafka.listener.ListenerExecutionFailedException: Listener failed; nested exception is java.lang.IllegalStateException: db connection fail
	at org.springframework.kafka.listener.SeekToCurrentBatchErrorHandler.handle(SeekToCurrentBatchErrorHandler.java:92) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.ContainerAwareBatchErrorHandler.handle(ContainerAwareBatchErrorHandler.java:56) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchErrorHandler(KafkaMessageListenerContainer.java:1626) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchListener(KafkaMessageListenerContainer.java:1500) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchListener(KafkaMessageListenerContainer.java:1380) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeListener(KafkaMessageListenerContainer.java:1363) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollAndInvoke(KafkaMessageListenerContainer.java:1082) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.run(KafkaMessageListenerContainer.java:990) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.run$$$capture(FutureTask.java:264) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java) ~[na:na]
	at java.base/java.lang.Thread.run(Thread.java:832) ~[na:na]
Caused by: org.springframework.kafka.listener.ListenerExecutionFailedException: Listener failed; nested exception is java.lang.IllegalStateException: db connection fail
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.decorateException(KafkaMessageListenerContainer.java:1928) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	... 10 common frames omitted
Caused by: java.lang.IllegalStateException: db connection fail
	at ru.galuzin.consumer.conf.ConsumerConfig$1.onMessage(ConsumerConfig.java:76) ~[classes/:na]
	at ru.galuzin.consumer.conf.ConsumerConfig$1.onMessage(ConsumerConfig.java:65) ~[classes/:na]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchOnMessage(KafkaMessageListenerContainer.java:1609) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchOnMessageWithRecordsOrList(KafkaMessageListenerContainer.java:1594) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchOnMessage(KafkaMessageListenerContainer.java:1552) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchListener(KafkaMessageListenerContainer.java:1487) ~[spring-kafka-2.5.5.RELEASE.jar:2.5.5.RELEASE]
	... 8 common frames omitted
	
=== 2.5.4 ===
2021-11-08 12:07:09.895 DEBUG 15896 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Commit list: {}
2021-11-08 12:07:10.400 DEBUG 15896 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Received: 1 records
2021-11-08 12:07:10.400  INFO 15896 --- [ consumer-0-C-1] ru.galuzin.consumer.conf.ConsumerConfig  : batch list size 1, test_galuzin_topic, 0, 51, 1 23
2021-11-08 12:07:10.400  INFO 15896 --- [ consumer-0-C-1] o.a.k.clients.consumer.KafkaConsumer     : [Consumer clientId=consumer-1, groupId=test_galuzin_consumer] Seeking to offset 51 for partition test_galuzin_topic-0
2021-11-08 12:07:10.770 DEBUG 15896 --- [ consumer-1-C-1] essageListenerContainer$ListenerConsumer : Received: 0 records
2021-11-08 12:07:10.770 DEBUG 15896 --- [ consumer-1-C-1] essageListenerContainer$ListenerConsumer : Commit list: {}
2021-11-08 12:07:13.401 ERROR 15896 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Error handler threw an exception

org.springframework.kafka.KafkaException: Seek to current after exception; nested exception is org.springframework.kafka.listener.ListenerExecutionFailedException: Listener failed; nested exception is java.lang.IllegalStateException: db connection fail
	at org.springframework.kafka.listener.SeekToCurrentBatchErrorHandler.handle(SeekToCurrentBatchErrorHandler.java:92) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.ContainerAwareBatchErrorHandler.handle(ContainerAwareBatchErrorHandler.java:56) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchErrorHandler(KafkaMessageListenerContainer.java:1624) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchListener(KafkaMessageListenerContainer.java:1498) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchListener(KafkaMessageListenerContainer.java:1378) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeListener(KafkaMessageListenerContainer.java:1361) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollAndInvoke(KafkaMessageListenerContainer.java:1080) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.run(KafkaMessageListenerContainer.java:988) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.run$$$capture(FutureTask.java:264) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java) ~[na:na]
	at java.base/java.lang.Thread.run(Thread.java:832) ~[na:na]
Caused by: org.springframework.kafka.listener.ListenerExecutionFailedException: Listener failed; nested exception is java.lang.IllegalStateException: db connection fail
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.decorateException(KafkaMessageListenerContainer.java:1926) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	... 10 common frames omitted
Caused by: java.lang.IllegalStateException: db connection fail
	at ru.galuzin.consumer.conf.ConsumerConfig$1.onMessage(ConsumerConfig.java:76) ~[classes/:na]
	at ru.galuzin.consumer.conf.ConsumerConfig$1.onMessage(ConsumerConfig.java:65) ~[classes/:na]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchOnMessage(KafkaMessageListenerContainer.java:1607) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchOnMessageWithRecordsOrList(KafkaMessageListenerContainer.java:1592) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchOnMessage(KafkaMessageListenerContainer.java:1550) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchListener(KafkaMessageListenerContainer.java:1485) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	... 8 common frames omitted

2021-11-08 12:07:13.401 DEBUG 15896 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Commit list: {}
2021-11-08 12:07:13.905 DEBUG 15896 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Received: 1 records
2021-11-08 12:07:13.905  INFO 15896 --- [ consumer-0-C-1] ru.galuzin.consumer.conf.ConsumerConfig  : batch list size 1, test_galuzin_topic, 0, 51, 1 24
2021-11-08 12:07:13.905  INFO 15896 --- [ consumer-0-C-1] o.a.k.clients.consumer.KafkaConsumer     : [Consumer clientId=consumer-1, groupId=test_galuzin_consumer] Seeking to offset 51 for partition test_galuzin_topic-0
2021-11-08 12:07:15.771 DEBUG 15896 --- [ consumer-1-C-1] essageListenerContainer$ListenerConsumer : Received: 0 records
2021-11-08 12:07:15.771 DEBUG 15896 --- [ consumer-1-C-1] essageListenerContainer$ListenerConsumer : Commit list: {}
2021-11-08 12:07:16.906 ERROR 15896 --- [ consumer-0-C-1] essageListenerContainer$ListenerConsumer : Error handler threw an exception

org.springframework.kafka.KafkaException: Seek to current after exception; nested exception is org.springframework.kafka.listener.ListenerExecutionFailedException: Listener failed; nested exception is java.lang.IllegalStateException: db connection fail
	at org.springframework.kafka.listener.SeekToCurrentBatchErrorHandler.handle(SeekToCurrentBatchErrorHandler.java:92) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.ContainerAwareBatchErrorHandler.handle(ContainerAwareBatchErrorHandler.java:56) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchErrorHandler(KafkaMessageListenerContainer.java:1624) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchListener(KafkaMessageListenerContainer.java:1498) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchListener(KafkaMessageListenerContainer.java:1378) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeListener(KafkaMessageListenerContainer.java:1361) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollAndInvoke(KafkaMessageListenerContainer.java:1080) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.run(KafkaMessageListenerContainer.java:988) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.run$$$capture(FutureTask.java:264) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java) ~[na:na]
	at java.base/java.lang.Thread.run(Thread.java:832) ~[na:na]
Caused by: org.springframework.kafka.listener.ListenerExecutionFailedException: Listener failed; nested exception is java.lang.IllegalStateException: db connection fail
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.decorateException(KafkaMessageListenerContainer.java:1926) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	... 10 common frames omitted
Caused by: java.lang.IllegalStateException: db connection fail
	at ru.galuzin.consumer.conf.ConsumerConfig$1.onMessage(ConsumerConfig.java:76) ~[classes/:na]
	at ru.galuzin.consumer.conf.ConsumerConfig$1.onMessage(ConsumerConfig.java:65) ~[classes/:na]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchOnMessage(KafkaMessageListenerContainer.java:1607) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchOnMessageWithRecordsOrList(KafkaMessageListenerContainer.java:1592) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeBatchOnMessage(KafkaMessageListenerContainer.java:1550) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeBatchListener(KafkaMessageListenerContainer.java:1485) ~[spring-kafka-2.5.4.RELEASE.jar:2.5.4.RELEASE]
	... 8 common frames omitted
