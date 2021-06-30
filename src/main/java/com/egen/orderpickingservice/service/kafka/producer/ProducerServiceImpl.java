package com.egen.orderpickingservice.service.kafka.producer;

import com.egen.orderpickingservice.dto.OrdersDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProducerServiceImpl {

    private final KafkaTemplate<String, List<OrdersDto>> orderDtokafkaTemplate;

    @Value("${kafka.topic.order.name}")
    private String JSON_TOPIC;

    public ProducerServiceImpl(KafkaTemplate<String, List<OrdersDto>> orderDtokafkaTemplate) {
        this.orderDtokafkaTemplate = orderDtokafkaTemplate;
    }

    public void sendOrderData(List<OrdersDto> orderDto){
        log.info(String.format("$$$$ => Producing message: %s",orderDto));

        orderDtokafkaTemplate.executeInTransaction(t ->{
            ListenableFuture<SendResult<String,List<OrdersDto>>> future =  t .send(JSON_TOPIC,
                    "BO" + UUID.randomUUID().toString(),orderDto);
            future.addCallback(new ListenableFutureCallback<SendResult<String, List<OrdersDto>>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.info("Unable to produce message [ {} ] due to: {}",orderDto,throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, List<OrdersDto>> stringOrderDtoSendResult) {
                    log.info("Sent Message [ {} ] with offset=[ {} ]",orderDto,
                            stringOrderDtoSendResult.getRecordMetadata().offset());
                }
            });
            return true;
        });
    }
}