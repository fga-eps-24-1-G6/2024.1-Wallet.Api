package com.walletapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "companies", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Companies {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "ipo")
    private Integer ipo;

    @Column(name = "foundation_year")
    private Integer foundationYear;

    @Column(name = "firm_value")
    private BigDecimal firmValue;

    @Column(name = "number_of_papers")
    private BigDecimal numberOfPapers;

    @Column(name = "market_segment")
    private String marketSegment;

    @Column(name = "sector")
    private String sector;

    @Column(name = "segment")
    private String segment;
}
