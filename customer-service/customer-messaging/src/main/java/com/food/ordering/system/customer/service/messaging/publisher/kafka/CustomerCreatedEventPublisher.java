package com.food.ordering.system.customer.service.messaging.publisher.kafka;

import com.food.ordering.system.customer.service.domain.config.CustomerServiceConfigData;
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.customer.service.domain.port.output.message.publisher.CustomerMessagePublisher;
import com.food.ordering.system.customer.service.messaging.mapper.CustomerMessagingDataMapper;
import com.food.ordering.system.kafka.order.avro.model.CustomerAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class CustomerCreatedEventPublisher implements CustomerMessagePublisher {

    private final CustomerMessagingDataMapper customerMessagingDataMapper;
    private final KafkaProducer<String, CustomerAvroModel> kafkaProducer;
    private final CustomerServiceConfigData customerServiceConfigData;

    public CustomerCreatedEventPublisher(CustomerMessagingDataMapper customerMessagingDataMapper,
                                         KafkaProducer<String, CustomerAvroModel> kafkaProducer,
                                         CustomerServiceConfigData customerServiceConfigData) {
        this.customerMessagingDataMapper = customerMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.customerServiceConfigData = customerServiceConfigData;
    }

    @Override
    public void publish(CustomerCreatedEvent customerCreatedEvent) {
        log.info("Received CustomerCreatedEvent for customer id: {}",
                customerCreatedEvent.getCustomer().getId());

        try{
            CustomerAvroModel customerAvroModel = customerMessagingDataMapper.
                    customerCreateToCustomerAvroModel(customerCreatedEvent);
            kafkaProducer.send(customerServiceConfigData.getCustomerTopicName(), customerAvroModel.getId(),
                    customerAvroModel, 
                    getCallBack(customerServiceConfigData.getCustomerTopicName(),customerAvroModel));
            log.info("CustomerCreatedEvent sent to kafka for customer id: {}",
                    customerCreatedEvent.getCustomer().getId());
        }catch (Exception e){
            log.error("Error while sending CustomerCreateEvent to kafka for customer id: {} error :{}"+
                    customerCreatedEvent.getCustomer().getId().getValue(), e.getMessage());
        }
    }

    private ListenableFutureCallback<SendResult<String, CustomerAvroModel>>
                                getCallBack(String customerTopicName,
                                            CustomerAvroModel customerAvroModel) {
        return new ListenableFutureCallback<SendResult<String, CustomerAvroModel>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending message {} to topic {}", customerAvroModel.toString(),
                        customerTopicName, ex);
            }

            @Override
            public void onSuccess(SendResult<String, CustomerAvroModel> result) {
                RecordMetadata recordMetadata = result.getRecordMetadata();
                log.info("Received new metadata. Topic: {}; Partition: {}; Offset: {}; Timestamp: {}, at time {}",
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.offset(),
                        recordMetadata.timestamp(),
                        System.nanoTime());
            }
        };

    }
}
