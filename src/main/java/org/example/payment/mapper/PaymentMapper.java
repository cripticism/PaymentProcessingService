package org.example.payment.mapper;

import org.example.payment.dto.PaymentDTO;
import org.example.payment.model.Payment;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentDTO dto) {
        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setCurrency(dto.getCurrency());
        payment.setFromAccount(dto.getFromAccount());
        payment.setToAccount(dto.getToAccount());
        payment.setTimestamp(Timestamp.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        return payment;
    }

    public PaymentDTO toDto(Payment payment) {
        return PaymentDTO.builder()
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .fromAccount(payment.getFromAccount())
                .toAccount(payment.getToAccount())
                .build();
    }
}
