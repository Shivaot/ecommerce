package com.tothenew.ecommerceapp.Entities.Users;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.Valid;


@Entity
@PrimaryKeyJoinColumn(name="user_id")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFilter("ignoreAddressInCustomer")
public class Customer extends User {

    private String contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
