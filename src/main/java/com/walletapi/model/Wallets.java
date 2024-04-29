package com.walletapi.model;

import javax.persistence.*;
import lombok.*;


@Entity
@Table(name = "wallets", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Wallets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;


}
