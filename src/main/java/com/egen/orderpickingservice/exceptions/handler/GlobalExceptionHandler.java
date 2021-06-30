package com.egen.orderpickingservice.exceptions.handler;

import com.egen.orderpickingservice.exceptions.EmployeeNotFoundException;
import com.egen.orderpickingservice.exceptions.OrderPickingServiceException;
import com.egen.orderpickingservice.response.Response;
import com.egen.orderpickingservice.response.ResponseMetadata;
import com.egen.orderpickingservice.response.StatusMessage;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({OrderPickingServiceException.class})
    public ResponseEntity<Response<?>> handleOrderPickingServiceException(OrderPickingServiceException ex) {
       log.error(ex.getMessage());
        return buildResponse(StatusMessage.UNKNOWN_INTERNAL_ERROR, INTERNAL_SERVER_ERROR, NOT_FOUND,CONFLICT);
    }
    @ExceptionHandler({EmployeeNotFoundException.class})
    public ResponseEntity<Response<?>> handleEmployeesServiceException(EmployeeNotFoundException ex) {
        log.error(ex.getMessage());
        return buildResponse(StatusMessage.UNKNOWN_INTERNAL_ERROR, INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, NOT_FOUND);
    }

    private ResponseEntity<Response<?>> buildResponse(StatusMessage statusMessage, HttpStatus serverError,
                                                      HttpStatus status, HttpStatus conflict) {
        var response = Response.builder()
                .meta(ResponseMetadata.builder()
                        .statusMessage(statusMessage.name())
                        .statusCode(status.value())
                        .build())
                .build();
        return ResponseEntity.status(status)
                .body(response);
    }
}
