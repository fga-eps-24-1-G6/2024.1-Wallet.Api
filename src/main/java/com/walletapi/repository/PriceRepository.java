package com.walletapi.repository;


import com.walletapi.model.Prices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Prices, Integer> {
    List<Prices> findAllByStockIdIdOrderByPriceDateDesc(Integer id);
}
