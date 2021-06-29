package com.egen.orderpickingservice.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPickingServiceException extends  RuntimeException {
    public static final long serialVersionUID = 1L;
    public OrderPickingServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderPickingServiceException(String message) {
        super(message);
    }

}
