FROM openjdk:8-jdk-alpine
ENV SPRING_DATASOURCE_URL=jdbc:mysql://18.219.191.9:3000/order_picking_service?useSSL=false
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=root
EXPOSE 8080
COPY ./target/order-picking-service-0.0.1-SNAPSHOT.jar order-picking-service.jar
CMD ["java","-jar","order-picking-service.jar"]
