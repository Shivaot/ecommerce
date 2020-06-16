package com.tothenew.ecommerceappAfterStage2Complete.repositories;

import com.tothenew.ecommerceappAfterStage2Complete.entities.order.OrderStatus;
import org.springframework.data.repository.CrudRepository;

public interface OrderStatusRepo extends CrudRepository<OrderStatus,Long> {
}
