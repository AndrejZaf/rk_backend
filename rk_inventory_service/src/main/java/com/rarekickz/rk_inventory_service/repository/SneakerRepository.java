package com.rarekickz.rk_inventory_service.repository;

import com.rarekickz.rk_inventory_service.domain.Sneaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SneakerRepository extends JpaRepository<Sneaker, Long>, JpaSpecificationExecutor<Sneaker> {

    @Query("SELECT s FROM sneaker s " +
            "LEFT JOIN FETCH s.sneakerImages " +
            "WHERE s.special = true")
    Optional<Sneaker> findBySpecialIsTrue();

    @Query("SELECT s FROM sneaker s " +
            "LEFT JOIN FETCH s.sneakerImages " +
            "WHERE s.id = :sneakerId")
    Optional<Sneaker> findByIdWithImages(Long sneakerId);

    @Query("SELECT s FROM sneaker s " +
            "LEFT JOIN FETCH s.sneakerImages " +
            "WHERE s.id IN :sneakerIds")
    List<Sneaker> findAllByIdWithImages(List<Long> sneakerIds);

    Optional<Sneaker> findBySpecialTrue();

    @Query("SELECT s FROM sneaker s " +
            "LEFT JOIN FETCH s.sneakerImages")
    List<Sneaker> findAllSneakersWithImages();

    @Query("SELECT s FROM sneaker s " +
            "LEFT JOIN FETCH s.sneakerSizes " +
            "WHERE s.id IN :ids")
    List<Sneaker> findAllWithSizes(Collection<Long> ids);
}
