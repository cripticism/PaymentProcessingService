package org.example.payment.repository;

import org.example.payment.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Page<Payment> findByAmountGreaterThan(Double amount, Pageable pageable);

    Page<Payment> findByAmountLessThan(Double amount, Pageable pageable);

    Page<Payment> findByAmount(Double amount, Pageable pageable);

    Page<Payment> findByAmountBetween(Double minAmount, Double maxAmount, Pageable pageable);
}
