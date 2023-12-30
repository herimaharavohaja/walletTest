package com.wallet.Haja.entity;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CurrencyValue {
    private Long currencyValueId;
    private int sourceCurrencyId;
    private int destinationCurrencyId;
    private Double value;
    private LocalDate effectiveDate;
}
