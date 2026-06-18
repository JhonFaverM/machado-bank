package com.machado.bank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransactionRequestDTO(
        @NotBlank
        @Size(min = 36, max = 36, message = "El número de cuenta debe tener 36 caracteres (formato UUID)")
        String accountNumber,

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
        BigDecimal amount
) {}
