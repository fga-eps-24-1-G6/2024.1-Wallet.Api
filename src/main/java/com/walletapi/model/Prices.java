package com.walletapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Table(name = "prices", schema = "public")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Prices {

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stocks stockId;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "price_date", nullable = false)
    private LocalDate priceDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}