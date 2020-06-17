package com.tothenew.ecommerceappAfterStage2Complete.dtos;

import java.io.Serializable;

public class RabbitMqOrderDTO implements Serializable {
    OrderDTO orderDTO;
    String email;

    public OrderDTO getOrderDTO() {
        return orderDTO;
    }

    public void setOrderDTO(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
