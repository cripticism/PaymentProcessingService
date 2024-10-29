package org.example.payment.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name="payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private String currency;
    private String fromAccount;
    private String toAccount;
    @Column(nullable = false) // Optional: Ensures timestamp is always set
    private Timestamp timestamp;

}
