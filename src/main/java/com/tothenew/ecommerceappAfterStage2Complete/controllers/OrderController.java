package com.tothenew.ecommerceappAfterStage2Complete.controllers;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.OrderDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.ResponseDTO;
import com.tothenew.ecommerceappAfterStage2Complete.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createNewOrder(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        logger.info("Inside create new order");
        orderService.createOrder(orderDTO,request);
        return new ResponseEntity<>(new ResponseDTO("Order Placed Successfully",new Date()), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public List<?> getAllOrders(HttpServletRequest request) {
        logger.info("Inside get all orders");
        return orderService.getAllOrders(request);
    }

    @PatchMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestBody Map<String,Long> payLoad) {
        logger.info("Inside cancel order");
        orderService.cancelOrder(payLoad.get("orderProductId"));
        return new ResponseEntity<>(new ResponseDTO("Order Cancelled Successfully",new Date()),HttpStatus.OK);
    }

    @PatchMapping("/return")
    public ResponseEntity<?> returnOrder(@RequestBody Map<String,Long> payLoad) {
        logger.info("Inside return order");
        orderService.returnOrder(payLoad.get("orderProductId"));
        return new ResponseEntity<>(new ResponseDTO("Order Returned Successfully",new Date()),HttpStatus.OK);
    }
}
