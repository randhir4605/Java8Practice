package com.example.demo;

import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class Practice {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;
    @GetMapping("/")
    public void java8practice() {
        List<Product> productList = productRepository.findAll();
        List<Customer> customerList = customerRepository.findAll();
        List<Order> orderList = orderRepository.findAll();

        //Check your data

        /*System.out.println("==========Product List =============");
        productList.forEach(product -> {
            System.out.println(product.getName());
        });

        System.out.println("==========Customer List =============");
        customerList.forEach(customer -> {
            System.out.println(customer.getName());
        });

        System.out.println("==========Order List with Order Items =============");
        orderList.forEach(order -> {
            System.out.println(order.getId());
            System.out.println(order.getCustomer().getName());
            order.getOrderItems().forEach(orderItems -> {
                System.out.println(orderItems.getProduct().getName() +" "+orderItems.getSellingPrice());
            });
        });*/

        //Java 8 practice

        //Exercise 1 — Obtain a list of products belongs to category “Books” with price > 100
        List<Product> list1= productList.stream().filter(product -> product.getCategory().equalsIgnoreCase("Book") && product.getPrice()>1000)
                .toList();

        list1.forEach(product -> System.out.println(product.getName()+" "+product.getPrice()));

        //  Exercise 2 — Obtain a list of order with products belong to category “Baby”


        //Exercise 15 — Get the most expensive product by category
        Map<String, Optional<Product>> expensiveProducts= productList.stream()
                .collect(Collectors.groupingBy(product -> product.getCategory().toLowerCase(),Collectors.maxBy(Comparator.comparingDouble(Product::getPrice))));
        expensiveProducts.forEach((s, product) -> System.out.println("Category :"+s+",  Most Expensive Item: "+product.get().getName()));

        //Exercise 13 — Produce a data map with order record and product total sum
        // order --> List<orderItem>  --> OrderItem --> sellingPrice --> sum
        /*
           select order_table.id, sum(order_items.selling_price) as total_amount
           from order_table,order_items
           where order_table.id=order_items.order_table_id
           group by order_table.id;
           */
        //sum of all orders. Understanding this is necessary to solve group by
        double totalSum=orderList.stream()
                .map(Order::getOrderItems)
                .flatMap(Collection::stream)
                .mapToDouble(OrderItems::getSellingPrice)
                .sum();
        System.out.println("Total Sales : "+totalSum);

        //group by orders
        Map<Long,Double> eachOrderAmount =  orderList.stream()
                .collect(Collectors.groupingBy(
                   Order::getId,                    //grouping by id, the key of the map
                   Collectors.mapping(              // This whole block will produce value for the map
                           Order::getOrderItems,            //input of mapping as List of orderItems
                           Collectors.flatMapping(          //flatMapping is required for stream of List
                                   Collection::stream,             //convert list of stream into stream of orderItems
                                   Collectors.mapping(             //convert the stream of orderItems to sellingPrice
                                           OrderItems::getSellingPrice,        //pass input as sellingPrice
                                           Collectors.summingDouble(Double::doubleValue)    //collect sellingPrice and Sum
                                   )
                           )
                   )
                ));

        eachOrderAmount.forEach((orderId,amount)-> System.out.println("Order Id - "+orderId+"  Total Amount - "+amount));
    }


}
