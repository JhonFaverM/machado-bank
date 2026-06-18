package com.machado.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. ERRORES DE VALIDACIÓN EN DTOs (Los @NotBlank, @Size, @DecimalMin de tus Records)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Extrae el mensaje que pusiste en el DTO (ej: "Cuenta de origen obligatoria")
        String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
    }

    // 2. RECURSO NO ENCONTRADO (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    // 3. MANEJO DE CONCURRENCIA (Optimistic Lock)
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiError> handleConcurrency(ObjectOptimisticLockingFailureException ex) {
        return new ResponseEntity<>(new ApiError(HttpStatus.CONFLICT.value(),
                "La operación no pudo completarse porque los datos fueron actualizados por otro usuario."), HttpStatus.CONFLICT);
    }

    // 4. MANEJO DE ERRORES DE NEGOCIO (Fondos insuficientes, cuenta inactiva)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleBusinessLogic(IllegalStateException ex) {
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // 5. ARGUMENTOS INVÁLIDOS (Montos negativos, cuentas iguales)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // 6. RECURSO DUPLICADO
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicateResource(DuplicateResourceException ex) {
        return new ResponseEntity<>(new ApiError(HttpStatus.CONFLICT.value(), ex.getMessage()), HttpStatus.CONFLICT);
    }

    // 7. ERRORES DE TIEMPO DE EJECUCIÓN (Como el de tu Service .orElseThrow)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // 8. ERROR GENERAL (El último recurso)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex) {
        ex.printStackTrace(); // Útil para debugear en consola
        return new ResponseEntity<>(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
