package com.food.ordering.system.order.dataaccess.customer.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers", schema = "customer")
@Entity
public class CustomerEntity {
    @Id
    private UUID id;

}