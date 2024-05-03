package com.walletapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class CompaniesDTO {
    private Integer ipo;
    private String sector;
    private String segment;
    private BigDecimal marketValue;
    private BigDecimal equity;
    private BigDecimal numberOfPapers;
}
