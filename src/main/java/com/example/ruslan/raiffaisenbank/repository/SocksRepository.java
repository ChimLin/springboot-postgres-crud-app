package com.example.ruslan.raiffaisenbank.repository;

import com.example.ruslan.raiffaisenbank.entity.SocksEntity;
import org.springframework.data.repository.CrudRepository;

public interface SocksRepository extends CrudRepository<SocksEntity, Long> {
    Iterable<SocksEntity> findByColor(String color);
}
