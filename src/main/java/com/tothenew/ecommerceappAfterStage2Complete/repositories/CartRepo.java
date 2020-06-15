package com.tothenew.ecommerceappAfterStage2Complete.repositories;

import com.tothenew.ecommerceappAfterStage2Complete.entities.order.Cart;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends CrudRepository<Cart,Long> {

    @Query(value = "select * from cart where product_variation_id=:varId AND customer_user_id=:custId",nativeQuery = true)
    Optional<Cart> findByVariationIdAndCustomerId(@Param("varId") Long varId, @Param("custId") Long custId);

    List<Cart> findByCustomer(Customer customer);

    @Modifying
    @Query(value = "delete from cart where id=:id",nativeQuery = true)
    void deleteByCartId(@Param("id") Long id);
}
