package com.rarekickz.rk_inventory_service.repository;

import com.rarekickz.rk_inventory_service.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
