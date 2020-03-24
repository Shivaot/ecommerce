package com.tothenew.ecommerceapp.Repositories;

import com.tothenew.ecommerceapp.Entities.Users.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepo extends CrudRepository<Address,Long> {
}
