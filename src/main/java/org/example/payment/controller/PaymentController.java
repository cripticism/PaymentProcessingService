package org.example.payment.controller;

import lombok.RequiredArgsConstructor;
import org.example.payment.dto.PaymentDTO;
import org.example.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // GET /payments: List all payments
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // POST /payments: Create a new payment
    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO) {
        PaymentDTO createdPayment = paymentService.createPayment(paymentDTO);
        return ResponseEntity.status(201).body(createdPayment);
    }

    // DELETE /payments/{id}: Delete a payment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        if (paymentService.paymentExists(id)) {
            paymentService.deletePayment(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
