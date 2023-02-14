package com.food.ordering.system.order.domain.valueobject;

import com.food.ordering.system.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {
    protected OrderItemId(Long value) {
        super(value);
    }
}
