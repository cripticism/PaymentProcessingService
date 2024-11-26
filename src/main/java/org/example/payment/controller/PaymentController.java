package org.example.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.payment.dto.PaymentDTO;
import org.example.payment.service.PaymentService;
import org.springdoc.core.annotations.ParameterObject;
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
    @Operation(summary = "Get all payments", description = "Retrieve a list of all payments")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping
    public ResponseEntity<Page<PaymentDTO>> getAllPayments(@ParameterObject Pageable pageable,
                                                           @RequestParam(required = false) Double amountGreaterThan,
                                                           @RequestParam(required = false) Double amountLessThan,
                                                           @RequestParam(required = false) Double amountEquals,
                                                           @RequestParam(required = false) Double minAmount,
                                                           @RequestParam(required = false) Double maxAmount) {

        Page<PaymentDTO> payments = paymentService.getAllPayments(pageable, amountGreaterThan, amountLessThan, amountEquals, minAmount, maxAmount);
        return ResponseEntity.ok(payments);
    }

    // POST /payments: Create a new payment
    @Operation(summary = "Create a new payment")
    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO createdPayment = paymentService.createPayment(paymentDTO);
        return ResponseEntity.status(201).body(createdPayment);
    }

    // PUT /payments/{id}: Update a payment by ID
    @Operation(summary = "Update a payment by ID")
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(@Valid @PathVariable Long id, @Valid @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO updatedPayment = paymentService.updatePayment(id, paymentDTO);
        return ResponseEntity.ok(updatedPayment);
    }

    // DELETE /payments/{id}: Delete a payment by ID
    @Operation(summary = "Delete a payment by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@Parameter(description = "ID of the payment to delete") @PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();

    }

}
