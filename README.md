## Introduction

This is the source code of project named food-ordering-system which has been developed using microservices and clean/hexagonal architecture. Following architecture patterns have been implemented as part of this project.

- CQRS
- Outbox
- SAGA
- Clean/Hexagonal Architecture

The project can be deployed on kubernetes. There are 4 microservices involved in this project named:

- customer-service
- order-service
- payment-service
- restaurant-service

#### Running Kafka using Docker on local
Create the following folders inside infrastructure/docker-compose folder:
- volumes/kafka/broker-1
- volumes/kafka/broker-2
- volumes/kafka/broker-3
- zookeeper/data
- zookeeper/transactions

These folders are mounted in docker-compose file.
CD into infrastructure/docker-compose folder. Run the following commands to bring up zookeeper:

```
docker-compose -f common.yml -f zookeeper.yml up -d
```
Run the following command to bring up kafka cluster:
```
docker-compose -f common.yml -f kafka-cluster.yml up -d
```
Run the following command to create topics getting used in project:
```
docker-compose -f common.yml -f init_kafka.yml up
```

_Note: Execute the following commands to bring down the kafka cluster._

```
docker-compose -f common.yml -f zookeeper.yml down
docker-compose -f common.yml -f kafka_cluster.yml down
```