# E-commerce Order Picking Service

## Developed Order Picking Services backend API  using Java, Spring Boot and Integrated with Kafka
--Added three new features--
- Create new employee
- Implemented Paging and Sorting technique-

---
## POST REQUEST

navigate to http://localhost:8080/api/order-picking/place-order to create a place new pick order

navigate to http://localhost:8080/api/order-picking/publish/order to Create a new order and Publish it to kafka consumer

---
## GET REQUESTS

navigate to http://localhost:8080/api/order-picking to fetch all the picked orders

navigate to http://localhost:8080/api/order-picking/{id} to fetch a specific picked order

navigate to http://localhost:8080/api/order-picking/order-status/{id} to fetch the status of the given picked order

navigate to http://localhost:8080/api/order-picking/pagelimit?pageNo=0&pageSize=10 to fetches all the orders by limiting 10 orders per page by default

navigate to http://localhost:8080/api/order-picking/sortby?pageNo=0&pageSize=10&sortBy=dateOrdered to get sorted values of your choice. by default sortBy is set to dateOrdered

---

## PUT REQUESTS

navigate to http://localhost:8080/api/order-picking/cancel-picked/{id} to cancel the picked order

---

## NOTE
Navigate to http://localhost:8080/api/swagger-ui.html#/ to view the Swagger Documentation

Refer the enums package to know the OrderStatus, Available Store, and Zone values
