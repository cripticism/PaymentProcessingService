package org.example.payment.service;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.example.payment.dto.PaymentDTO;
import org.example.payment.exception.InvalidPaymentException;
import org.example.payment.exception.PaymentNotFoundException;
import org.example.payment.mapper.PaymentMapper;
import org.example.payment.model.Payment;
import org.example.payment.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    // List all payments
    public Page<PaymentDTO> getAllPayments(Pageable pageable, Double amountGreaterThan, Double amountLessThan, Double amountEquals,
                                           Double minAmount, Double maxAmount) {
        Page<Payment> payments;

        if (amountGreaterThan != null) {
            payments = paymentRepository.findByAmountGreaterThan(amountGreaterThan, pageable);
        } else if (amountLessThan != null) {
            payments = paymentRepository.findByAmountLessThan(amountLessThan, pageable);
        } else if (amountEquals != null) {
            payments = paymentRepository.findByAmount(amountEquals, pageable);
        } else if (minAmount != null && maxAmount != null) {
            payments = paymentRepository.findByAmountBetween(minAmount, maxAmount, pageable);
        } else {
            // No filter applied, get all payments
            payments = paymentRepository.findAll(pageable);
        }
        // Map entities to DTOs
        return payments.map(paymentMapper::toDto);
    }

    // Create a new payment
    @Transactional
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        if (paymentDTO.getAmount() == null || paymentDTO.getAmount() <= 0) {
            throw new InvalidPaymentException("Invalid payment amount.");
        }
        Payment payment = paymentMapper.toEntity(paymentDTO);
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    // Update an existing payment
    @Transactional
    public PaymentDTO updatePayment(Long id, PaymentDTO paymentDTO) throws ConstraintViolationException {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));

        if (paymentDTO.getAmount() <= 0) {
            throw new InvalidPaymentException("Amount must be greater than zero.");
        }
        if (paymentDTO.getFromAccount().equals(paymentDTO.getToAccount())) {
            throw new InvalidPaymentException("Cannot transfer into the same account.");
        }
        payment.setAmount(paymentDTO.getAmount());
        payment.setCurrency(paymentDTO.getCurrency());
        payment.setFromAccount(paymentDTO.getFromAccount());
        payment.setToAccount(paymentDTO.getToAccount());

        Payment updatedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(updatedPayment);
    }

    // Delete a payment by ID
    @Transactional
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new PaymentNotFoundException(id);
        }
        paymentRepository.deleteById(id);
    }
}
