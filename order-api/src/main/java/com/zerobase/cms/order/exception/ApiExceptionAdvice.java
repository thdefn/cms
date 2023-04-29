package com.zerobase.cms.order.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApiExceptionAdvice {
    @ExceptionHandler({CustomException.class})
    public ResponseEntity<CustomException.CustomExceptionResponse> exceptionHandler(HttpServletRequest request, final CustomException exception){
        return ResponseEntity
                .status(exception.getStatus())
                .body(CustomException.CustomExceptionResponse.builder()
                        .message(exception.getMessage())
                        .code(exception.getErrorCode().name())
                        .status(exception.getStatus())
                        .build()
                );
    }
}
