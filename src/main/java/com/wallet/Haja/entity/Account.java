package com.wallet.Haja.entity;

import lombok.*;

import java.util.List;
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account {
    private Long accountId;
    private String accountName;
    private AccountType accountType;
    private List<Transaction> transactionList;
    private Balance balance;
    private Currency currency;
}
