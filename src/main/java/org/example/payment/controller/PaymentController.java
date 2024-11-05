package org.example.payment.controller;

import lombok.RequiredArgsConstructor;
import org.example.payment.dto.PaymentDTO;
import org.example.payment.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // GET /payments: List all payments
    @GetMapping
    public ResponseEntity<Page<PaymentDTO>> getAllPayments(Pageable pageable,
                                                           @RequestParam(required = false) Double amountGreaterThan,
                                                           @RequestParam(required = false) Double amountLessThan,
                                                           @RequestParam(required = false) Double amountEquals,
                                                           @RequestParam(required = false) Double minAmount,
                                                           @RequestParam(required = false) Double maxAmount) {

        Page<PaymentDTO> payments = paymentService.getAllPayments(pageable, amountGreaterThan, amountLessThan, amountEquals, minAmount, maxAmount);
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
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();

    }

}
