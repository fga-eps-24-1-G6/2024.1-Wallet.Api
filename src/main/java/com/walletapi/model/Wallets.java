package com.walletapi.model;

import javax.persistence.*;
import lombok.*;


@Entity
@Table(name = "wallets", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Wallets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "external_id", nullable = false)
    private String externalId;
}
