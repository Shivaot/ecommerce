package com.tothenew.ecommerceappAfterStage2Complete.repositories;

import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Seller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SellerRepo extends CrudRepository<Seller, java.lang.Long> {
    List<Seller> findByGst(String gst);
    @Query(value = "select gst from seller where gst=:gst",nativeQuery = true)
    String getByGst(@Param("gst") String gst);
    Seller findByCompanyName(String companyName);
    List<Seller> findAll(Pageable pageable);
    Seller findByEmail(String email);
}
