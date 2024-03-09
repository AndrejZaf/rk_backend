package com.rarekickz.rk_inventory_service.repository;

import com.rarekickz.rk_inventory_service.domain.SneakerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SneakerImageRepository extends JpaRepository<SneakerImage, Long> {

    List<SneakerImage> findBySneakerIdIn(Collection<Long> sneakerIds);
}
