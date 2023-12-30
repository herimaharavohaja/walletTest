package com.wallet.Haja.entity;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Balance {
    private Long balanceId;
    private LocalDateTime balanceDateTime;
    private Double amount;
    private Account account;
}
