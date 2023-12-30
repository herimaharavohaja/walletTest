package com.wallet.Haja.entity;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Currency {
    private Long currencyId;
    private String currencyName;
    private String currencyCode;
}
