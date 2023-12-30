package com.wallet.Haja.entity;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransferHistory {
    private Long transferHistoryId;
    private Account debitTransaction;
    private Account creditTransaction;
    private Double amount;
    private LocalDateTime transferDate;
}
