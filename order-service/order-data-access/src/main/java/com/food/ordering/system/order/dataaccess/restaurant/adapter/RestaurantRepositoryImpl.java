package com.food.ordering.system.order.dataaccess.restaurant.adapter;

import com.food.ordering.system.order.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.order.dataaccess.restaurant.repository.RestaurantJPARepository;
import com.food.ordering.system.order.domain.entity.Restaurant;
import com.food.ordering.system.order.service.ports.output.repository.RestaurantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJPARepository restaurantJPARepository;

    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public RestaurantRepositoryImpl(RestaurantJPARepository restaurantJPARepository,
                                    RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.restaurantJPARepository = restaurantJPARepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts = restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJPARepository
                .findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), restaurantProducts);
        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
