package com.milansomyk.bookstore.mapper;

import com.milansomyk.bookstore.dto.OrdersDto;
import com.milansomyk.bookstore.entity.Orders;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class OrdersMapper {
    private final UserMapper userMapper;
    public OrdersDto toDto(Orders orders){
        return OrdersDto.builder()
                .orderId(orders.getOrderId())
                .user(userMapper.toResponseDto(orders.getUser()))
                .books(orders.getBooks())
                .orderDate(orders.getOrderDate())
                .orderPrice(orders.getOrderPrice())
                .build();
    }
    public Orders fromDto(OrdersDto ordersDto){
        return new Orders(ordersDto.getOrderId(),userMapper.fromDto(ordersDto.getUser()), ordersDto.getBooks(), ordersDto.getOrderDate(), ordersDto.getOrderPrice());
    }
}
