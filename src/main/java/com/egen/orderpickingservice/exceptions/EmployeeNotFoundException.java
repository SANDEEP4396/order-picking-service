package com.egen.orderpickingservice.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeNotFoundException extends  RuntimeException {
    public static final long serialVersionUID = 1L;
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
