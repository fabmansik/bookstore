package com.milansomyk.bookstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@EnableAutoConfiguration
@AllArgsConstructor
@Data
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_books",
            joinColumns = @JoinColumn(name = "orders_id"),
            inverseJoinColumns = @JoinColumn(name = "books_id")
    )
    private List<Book> books;
    private Date orderDate;
    private int orderPrice;
}
