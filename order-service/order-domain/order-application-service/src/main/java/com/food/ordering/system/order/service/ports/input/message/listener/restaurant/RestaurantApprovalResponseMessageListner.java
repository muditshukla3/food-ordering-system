package com.food.ordering.system.order.service.ports.input.message.listener.restaurant;

import com.food.ordering.system.order.service.dto.message.RestaurantApprovalResponse;

public interface RestaurantApprovalResponseMessageListner {

    void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);

    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
