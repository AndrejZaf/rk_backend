package com.rarekickz.rk_inventory_service.repository;

import com.rarekickz.rk_inventory_service.domain.SneakerSize;
import com.rarekickz.rk_inventory_service.domain.SneakerSizeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SneakerSizeRepository extends JpaRepository<SneakerSize, SneakerSizeId> {

    @Query("SELECT DISTINCT ss.sneakerSizeId.size FROM sneaker_size ss")
    List<Double> findAllDistinctSizes();
}
