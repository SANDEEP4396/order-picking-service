package com.egen.orderpickingservice.service.kafka.consumer;

import com.egen.orderpickingservice.service.OrderPickingService;
import com.egen.orderpickingservice.dto.OrdersDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ConsumerServiceImpl {
    public OrderPickingService orderPickingService;

    @Autowired
    public ConsumerServiceImpl(OrderPickingService ordersService) {
        this.orderPickingService = ordersService;
    }

    /**
     * Collects the data from producer to place the order
     * @param offset - Provides Offset ID
     * @param partition - Provides Partition ID
     * @param key
     * @param ordersDto
     */
    @KafkaListener(containerFactory = "jsonKafkaListenerContainerFactory",
            topics = "${kafka.topic.order.name}",
            groupId = "${kafka.topic.order.groupId}")
    public void consumeOrderDetails(@Header(KafkaHeaders.OFFSET)Long offset,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID)Integer partition,
                                    @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY)String key,
                                    List<OrdersDto> ordersDto) throws ParseException {
        log.info("Consumed order: {} for batch: {} from Partition: {} at Offset: {}",key
                ,"BO"+ UUID.randomUUID(), partition,offset);

        //when Jackson attempts to deserialize an object in JSON, but no target type information is given,
        // it'll use the default type: LinkedHashMap. In other words, after the deserialization,
        // we'll get an ArrayList<LinkedHashMap> object. So convert it back to List<OrdersDto>

        ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.convertValue(ordersDto,JsonNode.class);
            ordersDto = mapper.convertValue(jsonNode, new TypeReference<List<OrdersDto>>(){});

        orderPickingService.createOrders(ordersDto);
    }
}
