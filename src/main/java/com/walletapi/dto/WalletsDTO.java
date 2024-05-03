package com.walletapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class WalletsDTO {
    private Integer id;
    private String name;
}
