package com.tothenew.ecommerceappAfterStage2Complete.repositories;

import com.tothenew.ecommerceappAfterStage2Complete.entities.order.OrderTable;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepo extends CrudRepository<OrderTable,Long> {

    List<OrderTable> findByCustomer(Customer customer);
}
