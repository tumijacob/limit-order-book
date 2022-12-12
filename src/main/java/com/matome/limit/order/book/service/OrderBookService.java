package com.matome.limit.order.book.service;

import com.matome.limit.order.book.model.Order;

import java.util.Queue;

public interface OrderBookService {

    Order addOrder(Order order, Queue<Order> ordersQueue);
    Order updateOrder(Long id, int quantity);
    void deleteOrder(Long id);


}
