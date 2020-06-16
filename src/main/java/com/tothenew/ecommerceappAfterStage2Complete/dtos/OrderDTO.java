package com.tothenew.ecommerceappAfterStage2Complete.dtos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class OrderDTO {

    private BigDecimal amountPaid;
    private Date date;
    private String paymentMethod;
    private String customerAddressCity;
    private String customerAddressState;
    private String customerAddressCountry;
    private String customerAddressLine;
    private Integer customerZipCode;
    private String customerAddressLabel;
    private Set<OrderProductDTO> productDTOS;

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCustomerAddressCity() {
        return customerAddressCity;
    }

    public void setCustomerAddressCity(String customerAddressCity) {
        this.customerAddressCity = customerAddressCity;
    }

    public String getCustomerAddressState() {
        return customerAddressState;
    }

    public void setCustomerAddressState(String customerAddressState) {
        this.customerAddressState = customerAddressState;
    }

    public String getCustomerAddressCountry() {
        return customerAddressCountry;
    }

    public void setCustomerAddressCountry(String customerAddressCountry) {
        this.customerAddressCountry = customerAddressCountry;
    }

    public String getCustomerAddressLine() {
        return customerAddressLine;
    }

    public void setCustomerAddressLine(String customerAddressLine) {
        this.customerAddressLine = customerAddressLine;
    }

    public Integer getCustomerZipCode() {
        return customerZipCode;
    }

    public void setCustomerZipCode(Integer customerZipCode) {
        this.customerZipCode = customerZipCode;
    }

    public String getCustomerAddressLabel() {
        return customerAddressLabel;
    }

    public void setCustomerAddressLabel(String customerAddressLabel) {
        this.customerAddressLabel = customerAddressLabel;
    }

    public Set<OrderProductDTO> getProductDTOS() {
        return productDTOS;
    }

    public void setProductDTOS(Set<OrderProductDTO> productDTOS) {
        this.productDTOS = productDTOS;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "amountPaid=" + amountPaid +
                ", date=" + date +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", customerAddressCity='" + customerAddressCity + '\'' +
                ", customerAddressState='" + customerAddressState + '\'' +
                ", customerAddressCountry='" + customerAddressCountry + '\'' +
                ", customerAddressLine='" + customerAddressLine + '\'' +
                ", customerZipCode=" + customerZipCode +
                ", customerAddressLabel='" + customerAddressLabel + '\'' +
                ", productDTOS=" + productDTOS +
                '}';
    }
}
