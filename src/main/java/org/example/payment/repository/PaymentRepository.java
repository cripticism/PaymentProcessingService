package org.example.payment.repository;

import org.example.payment.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find payments where amount is greater than a specified value
    Page<Payment> findByAmountGreaterThan(Double amount, Pageable pageable);

    // Find payments where amount is less than a specified value
    Page<Payment> findByAmountLessThan(Double amount, Pageable pageable);

    // Find payments where amount is equal to a specified value
    Page<Payment> findByAmount(Double amount, Pageable pageable);

    // Find payments where amount is between two values
    Page<Payment> findByAmountBetween(Double minAmount, Double maxAmount, Pageable pageable);
}
