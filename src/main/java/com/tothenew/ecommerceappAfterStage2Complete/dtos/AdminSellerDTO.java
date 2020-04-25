package com.tothenew.ecommerceappAfterStage2Complete.dtos;

import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Address;

import java.util.Set;

public class AdminSellerDTO extends AdminCustomerDTO{
    private String companyName;
    private String companyContact;
    private Set<Address> companyAddress;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public Set<Address> getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(Set<Address> companyAddress) {
        this.companyAddress = companyAddress;
    }
}
