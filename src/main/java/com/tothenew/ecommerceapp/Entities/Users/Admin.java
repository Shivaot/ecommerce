package com.tothenew.ecommerceapp.Entities.Users;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.Date;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {

}
