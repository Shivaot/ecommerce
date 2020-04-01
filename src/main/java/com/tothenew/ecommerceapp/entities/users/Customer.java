package com.tothenew.ecommerceapp.entities.users;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;


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
