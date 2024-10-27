package org.example.payment.service;

import org.example.payment.model.Payment;
import org.example.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // List all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Create a new payment
    public Payment createPayment(Payment payment) {
        return paymentRepository.saveAndFlush(payment);
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
