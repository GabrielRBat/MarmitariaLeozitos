package com.marmitaria.repository;

import com.marmitaria.model.Marmita;
import java.util.List;

public interface MarmitaRepository {
    Marmita save(Marmita marmita);
    List<Marmita> findAll();
    Marmita findById(Long id);
    void update(Marmita marmita);
    void delete(Long id);
}
