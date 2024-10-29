package org.example.payment.service;

import lombok.RequiredArgsConstructor;
import org.example.payment.dto.PaymentDTO;
import org.example.payment.mapper.PaymentMapper;
import org.example.payment.model.Payment;
import org.example.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    // List all payments
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    // Create a new payment
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Payment payment = paymentMapper.toEntity(paymentDTO);
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    // Delete a payment by ID
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    // Check if payments exists by ID
    public boolean paymentExists(Long id) {
        return paymentRepository.existsById(id);
    }
}
