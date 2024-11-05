package org.example.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
@ToString
public class PaymentDTO {
    @NotNull(message = "Amount is required.")
    private Double amount;
    @NotBlank(message = "Currency is required.")
    private String currency;
    @NotBlank(message = "FromAccount is required.")
    private String fromAccount;
    @NotBlank(message = "ToAccount is required.")
    private String toAccount;
}
