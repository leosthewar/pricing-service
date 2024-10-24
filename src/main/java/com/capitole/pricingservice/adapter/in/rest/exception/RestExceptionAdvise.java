package com.capitole.pricingservice.adapter.in.rest.exception;


import com.capitole.pricingservice.adapter.in.rest.dto.ErrorResponseDTO;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.format.DateTimeParseException;
/**
 * This class contains exception handlers for the REST controller.
 *
 * <p>This class is used to handle exceptions that are thrown by beans annotated with {@link org.springframework.web.bind.annotation.RestController}.
 *
 * @author Leonardo Rincon - leo.sthewar.rincon@gmail.com
 * @see RestControllerAdvice
 */
@RestControllerAdvice
class RestExceptionAdvise {
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionAdvise.class);

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponseDTO> handleDateTimeParseException(DateTimeParseException e) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message("Invalid application date format. Please use yyyy-MM-dd HH:mm:ss.")
                .code("INVALID_DATE_FORMAT")
                .details(e.getMessage())
                .build();
        logger.warn("Invalid application date format. exception: {}",e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(ConstraintViolationException e) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message("Invalid price query. Please check the input parameters.")
                .code("INVALID_PRICE_QUERY")
                .details(e.getMessage())
                .build();
        logger.warn("Invalid price query. exception: {}",e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResponseDTO> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message("Invalid price query. Please check the input parameters.")
                .code("INVALID_PRICE_QUERY")
                .details(e.getMessage())
                .build();
        logger.warn("Invalid price query. exception: {}",e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoResourceFoundException(NoResourceFoundException e) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message("Resource not found.")
                .code(HttpStatus.NOT_FOUND.name())
                .details(e.getMessage())
                .build();
        logger.warn("Resource not found. exception: {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlePriceNotFoundException(PriceNotFoundException e) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message("Price not found.")
                .code(HttpStatus.NOT_FOUND.name())
                .details(e.getMessage())
                .build();
        logger.warn("Price not found. exception: {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception e) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message("An unexpected error occurred.")
                .code(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .details(e.getMessage())
                .build();
        logger.error("An unexpected error occurred. exception: {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }





}
