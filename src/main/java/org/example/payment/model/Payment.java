package org.example.payment.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Payment {

    private Long id;
    private Double amount;
    private String currency;
    private String fromAccount;
    private String toAccount;
    private Timestamp timestamp;

}
