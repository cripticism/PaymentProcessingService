package org.example.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
@ToString
public class PaymentDTO {
    @NotNull(message = "Amount is required.")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero.")
    private Double amount;
    @NotBlank(message = "Currency is required.")
    @Pattern(regexp = "USD|EUR|GBP", message = "Currency must be either USD, EUR or GBP.")
    private String currency;
    @NotBlank(message = "FromAccount is required.")
    private String fromAccount;
    @NotBlank(message = "ToAccount is required.")
    private String toAccount;
}
