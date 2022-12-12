package com.matome.limit.order.book.service.impl;

import com.matome.limit.order.book.enums.Side;
import com.matome.limit.order.book.model.Order;
import com.matome.limit.order.book.repository.OrderRepository;
import com.matome.limit.order.book.service.OrderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

@Service
public class OrderBookServiceImpl implements OrderBookService {
    private OrderRepository orderRepository;
    private PriorityQueue<Order> buyOrders;
    private PriorityQueue<Order> sellOrders;
    private Map<Long, Order> orders;
    private Map<Long,Order> filled_orders;

    @Autowired
    public OrderBookServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        buyOrders = getBuyOrSellOrders();
        sellOrders = getBuyOrSellOrders();
        orders = new HashMap<>();
        filled_orders = new HashMap<>();
    }


    @Override
    public Order addOrder(Order order, Queue<Order> ordersQueue) {
        //TODO: Include the limit
        final String side = order.getSide().toString();
        final String buy = Side.BUY.toString();
        final String sell = Side.SELL.toString();
        if (side.equalsIgnoreCase(buy)) {
            orders.put(order.getId(), order);
        } else if (side.equalsIgnoreCase(sell)) {
            orders.put(order.getId(), order);
        }
        int placedOrderTradeCount = order.getTradeCount();

        while (!ordersQueue.isEmpty()) {
            Order currentOrderFromQueue = ordersQueue.peek();
            int currentOrderFromQueueCount = currentOrderFromQueue.getTradeCount();
            if (side.equalsIgnoreCase(buy)) {
                if (order.getPrice().compareTo(currentOrderFromQueue.getPrice()) == 1) {
                    placedOrderTradeCount = processOrder(placedOrderTradeCount, ordersQueue,
                            currentOrderFromQueue, currentOrderFromQueueCount);
                } else {
                    break;
                }
            } else {
                if (order.getPrice().compareTo(currentOrderFromQueue.getPrice()) == -1) {
                    placedOrderTradeCount = processOrder(placedOrderTradeCount, ordersQueue,
                            currentOrderFromQueue, currentOrderFromQueueCount);
                } else {
                    break;
                }
            }
        }

        if (placedOrderTradeCount > 0) {
            try {
                Order partialRemainingOrder = (Order) order.clone();
                partialRemainingOrder.setTradeCount(placedOrderTradeCount);
            } catch (CloneNotSupportedException e) {
                //Logger.("Clone exception occured: " + e.getMessage());
            }
        }
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long id, int quantity) {
        if (orders.containsKey(id)) {

        }


        Order order = orderRepository.getOne(id);
        order.setQuantity(quantity);

        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        if (orders.containsKey(id)) {
            if (orders.get(id).getSide().toString().equalsIgnoreCase(Side.BUY.toString())) {
                buyOrders.remove(orders.get(id));
            } else {
                sellOrders.remove(orders.get(id));
            }
            orders.remove(id);
        }
        Order order = orderRepository.getOne(id);
        orderRepository.delete(order);
    }

    private PriorityQueue<Order> getBuyOrSellOrders() {
        return new PriorityQueue<>((o1, o2) -> {
            if (o1.getPrice().compareTo(o2.getPrice()) == -1) {
                return 1;
            } else if (o1.getPrice().compareTo(o2.getPrice()) == 1) {
                return -1;
            } else {
                if (o1.getTimestamp().compareTo(o2.getTimestamp()) == -1) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    private int processOrder(int placedOrderTradeCount, Queue ordersQueue, Order currentOrderFromQueue, int currentOrderFromQueueCount) {
        if (currentOrderFromQueueCount <= placedOrderTradeCount) {
            processOrderFromQueue(currentOrderFromQueue, currentOrderFromQueueCount);
            ordersQueue.poll();
            placedOrderTradeCount = placedOrderTradeCount - currentOrderFromQueueCount;
        } else {
            int currentFilledSellOrder = currentOrderFromQueueCount - placedOrderTradeCount;
            currentOrderFromQueue.setTradeCount(currentFilledSellOrder);
            placedOrderTradeCount = 0;
        }
        return placedOrderTradeCount;
    }

    private void processOrderFromQueue(Order currentOrderFromQueue, int currentOrderFromQueueCount) {
        Long currentOrderFromQueueId = currentOrderFromQueue.getId();
        if (filled_orders.containsKey(currentOrderFromQueueId)) {
            filled_orders.get(currentOrderFromQueueId).setTradeCount(filled_orders
                    .get(currentOrderFromQueueId).getTradeCount() + currentOrderFromQueueCount);
        } else {
            filled_orders.put(currentOrderFromQueueId, currentOrderFromQueue);
        }
    }

}
