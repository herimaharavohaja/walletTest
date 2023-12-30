package com.wallet.Haja.entity;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transaction {
    private Long transactionId;
    private String label;
    private Double amount;
    private LocalDateTime dateTime;
    private TransactionType transactionType;
    private Account account;
}
