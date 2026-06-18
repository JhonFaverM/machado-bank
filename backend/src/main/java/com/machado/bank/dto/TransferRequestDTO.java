package com.machado.bank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequestDTO(

        @NotBlank
        @Size(min = 36, max = 36, message = "El número de cuenta debe tener 36 caracteres (formato UUID)")
        String fromAccountNumber,

        @NotBlank
        @Size(min = 36, max = 36, message = "El número de cuenta debe tener 36 caracteres (formato UUID)")
        String toAccountNumber,

        @NotNull(message = "Monto obligatorio")
        @DecimalMin(value = "0.01", message = "Monto mínimo 0.01")
        BigDecimal amount

) {}
