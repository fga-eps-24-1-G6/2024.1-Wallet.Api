package com.walletapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transactions", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallets wallets;

    @ManyToOne
    @JoinColumn(name = "stocks_id", nullable = false)
    private Stocks stocks;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "operation", nullable = false)
    private String operation;
}
