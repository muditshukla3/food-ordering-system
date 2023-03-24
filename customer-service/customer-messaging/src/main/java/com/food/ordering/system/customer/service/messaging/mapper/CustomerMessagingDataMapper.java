package com.food.ordering.system.customer.service.messaging.mapper;

import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.kafka.order.avro.model.CustomerAvroModel;
import org.springframework.stereotype.Component;

@Component
public class CustomerMessagingDataMapper {

    public CustomerAvroModel customerCreateToCustomerAvroModel(CustomerCreatedEvent createdEvent){
        return CustomerAvroModel.newBuilder()
                .setId(createdEvent.getCustomer().getId().getValue().toString())
                .setUsername(createdEvent.getCustomer().getUsername())
                .setFirstName(createdEvent.getCustomer().getFirstName())
                .setLastName(createdEvent.getCustomer().getLastName())
                .build();
    }
}
