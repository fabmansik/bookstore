package com.milansomyk.bookstore.repository;

import com.milansomyk.bookstore.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
}
