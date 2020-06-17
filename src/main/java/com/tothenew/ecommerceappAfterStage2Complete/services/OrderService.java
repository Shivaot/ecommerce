package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.OrderDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.OrderProductDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.RabbitMqOrderDTO;
import com.tothenew.ecommerceappAfterStage2Complete.entities.enums.FromStatus;
import com.tothenew.ecommerceappAfterStage2Complete.entities.enums.ToStatus;
import com.tothenew.ecommerceappAfterStage2Complete.entities.order.OrderProduct;
import com.tothenew.ecommerceappAfterStage2Complete.entities.order.OrderStatus;
import com.tothenew.ecommerceappAfterStage2Complete.entities.order.OrderTable;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Customer;
import com.tothenew.ecommerceappAfterStage2Complete.entities.utils.AuditingInformation;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.ResourceNotFoundException;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.*;
import com.tothenew.ecommerceappAfterStage2Complete.utils.EmailSender;
import com.tothenew.ecommerceappAfterStage2Complete.utils.UserEmailFromToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private OrderProductRepo orderProductRepo;
    @Autowired
    private OrderStatusRepo orderStatusRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private UserEmailFromToken userEmailFromToken;
    @Autowired
    private ProductVariationRepo productVariationRepo;
    @Autowired
    private EmailSender emailSender;

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    public void createOrder(RabbitMqOrderDTO rabbitMqOrderDTO) {
        OrderTable orderTable = orderDTOToOrderTableConverter(rabbitMqOrderDTO.getOrderDTO(),rabbitMqOrderDTO.getEmail());
        Set<OrderProduct> orderProducts = new HashSet<>();
        rabbitMqOrderDTO.getOrderDTO().getProductDTOS().forEach(orderProductDTO -> {
            orderProductDTOToOrderProductConverter(orderProductDTO,orderTable,orderProducts);
        });
        orderRepo.save(orderTable);
        emailSender.sendEmail("ORDER PLACED","Your order has been successfully placed",rabbitMqOrderDTO.getEmail());
        logger.info("Order created success");
    }

    public List<?> getAllOrders(HttpServletRequest request) {
        Customer customer = customerRepo.findByEmail(userEmailFromToken.getUserEmail(request));
        List<OrderTable> orders = orderRepo.findByCustomer(customer);
        return orders;
    }

    public void cancelOrder(Long id) {
        Optional<OrderProduct> orderProduct = orderProductRepo.findById(id);
        if (!orderProduct.isPresent()) {
            throw new ResourceNotFoundException("Order " + id + " doesnt exist");
        }
        orderProduct.get().getOrderStatuses().forEach(orderStatus -> {
            if (orderStatus.getOrderProduct().getId().compareTo(id) == 0) {
                changeOrderStatus(orderProduct,ToStatus.CANCELLED);
            }
        });
        orderProductRepo.save(orderProduct.get());
        emailSender.sendEmail("ORDER CANCELLED","Your order has been successfully cancelled",orderProduct.get().getOrderTable().getCustomer().getEmail());
    }

    public void returnOrder(Long id) {
        Optional<OrderProduct> orderProduct = orderProductRepo.findById(id);
        if (!orderProduct.isPresent()) {
            throw new ResourceNotFoundException("Order " + id + " doesnt exist");
        }
        orderProduct.get().getOrderStatuses().forEach(orderStatus -> {
            if (orderStatus.getOrderProduct().getId().compareTo(id) == 0) {
                changeOrderStatus(orderProduct,ToStatus.RETURN_APPROVED);
            }
        });
        orderProductRepo.save(orderProduct.get());
        emailSender.sendEmail("ORDER RETURNED INITIATED","Your order has been successfully returned",orderProduct.get().getOrderTable().getCustomer().getEmail());

    }

    private OrderTable orderDTOToOrderTableConverter(OrderDTO orderDTO,String email) {
        Customer customer = customerRepo.findByEmail(email);
        OrderTable orderTable = new OrderTable();
        orderTable.setAmountPaid(orderDTO.getAmountPaid());
        orderTable.setPaymentMethod(orderDTO.getPaymentMethod());
        orderTable.setCustomer(customer);
        orderTable.setCustomerAddressAddressLine(orderDTO.getCustomerAddressLine());
        orderTable.setCustomerAddressCity(orderDTO.getCustomerAddressCity());
        orderTable.setCustomerAddressState(orderDTO.getCustomerAddressState());
        orderTable.setCustomerAddressCountry(orderDTO.getCustomerAddressCountry());
        orderTable.setCustomerAddressZipCode(orderDTO.getCustomerZipCode());
        orderTable.setCustomerAddressLabel(orderDTO.getCustomerAddressLabel());
        AuditingInformation auditingInformation = new AuditingInformation();
        auditingInformation.setDateCreated(orderDTO.getDate());
        auditingInformation.setCreatedBy(customer.getFirstName());
        auditingInformation.setLastUpdated(orderDTO.getDate());
        auditingInformation.setUpdatedBy(customer.getFirstName());
        orderTable.setAuditingInformation(auditingInformation);

        return orderTable;
    }

    private OrderTable orderProductDTOToOrderProductConverter(OrderProductDTO orderProductDTO,OrderTable orderTable,Set<OrderProduct> orderProducts) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setQuantity(orderProductDTO.getQuantity());
        orderProduct.setPrice(orderProductDTO.getPrice());
        orderProduct.setProductVariation(productVariationRepo.findById(orderProductDTO.getProductVariationId()).get());
        orderProduct.setProductVariationMetadata(orderProductDTO.getMetadata());
        orderProduct.setOrderTable(orderTable);
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setFromStatus(FromStatus.ORDER_PLACED);
        orderStatus.setToStatus(ToStatus.ORDER_CONFIRMED);
        orderStatus.setTransitionNotesComments("Order Confirmed");
        orderStatus.setOrderProduct(orderProduct);
        Set<OrderStatus> orderStatuses = new HashSet<>();
        orderStatuses.add(orderStatus);
        orderProduct.setOrderStatuses(orderStatuses);
        orderProducts.add(orderProduct);
        orderTable.setOrderProducts(orderProducts);

        return orderTable;
    }

    private Optional<OrderProduct> changeOrderStatus(Optional<OrderProduct> orderProduct,ToStatus toStatus) {
            OrderStatus status = new OrderStatus();
            status.setFromStatus(FromStatus.ORDER_CONFIRMED);
            status.setToStatus(toStatus);
            status.setTransitionNotesComments("order " + toStatus.toString());
            status.setOrderProduct(orderProduct.get());
            Set<OrderStatus> orderStatuses = orderProduct.get().getOrderStatuses();
            orderStatuses.add(status);
            orderProduct.get().setOrderStatuses(orderStatuses);
            orderProduct.get().getOrderTable().getAuditingInformation().setLastUpdated(new Date());
            orderProduct.get().getOrderTable().getAuditingInformation().setUpdatedBy(orderProduct.get().getOrderTable().getCustomer().getFirstName());
            return orderProduct;
    }

}
