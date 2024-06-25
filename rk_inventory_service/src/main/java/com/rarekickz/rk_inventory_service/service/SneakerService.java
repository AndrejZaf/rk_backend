package com.rarekickz.rk_inventory_service.service;

import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.dto.ReserveSneakerDTO;
import com.rarekickz.rk_inventory_service.dto.SneakerDTO;
import com.rarekickz.rk_inventory_service.enums.Gender;

import java.util.Collection;
import java.util.List;

public interface SneakerService {

    Sneaker findPremiumSneaker();

    Sneaker findMostPopularSneaker();

    Sneaker findById(Long sneakerId);

    List<Sneaker> findAllByPages(int page, int size, List<Long> brandIds, List<Gender> genders, List<Double> sizes, String sort, String name);

    Sneaker create(SneakerDTO sneakerDTO);

    Sneaker update(SneakerDTO sneakerDTO);

    void deleteById(Long sneakerId);

    void premium(Long sneakerId);

    List<Sneaker> findAll();

    List<Sneaker> findAllByIdWithImages(List<Long> sneakerIds);

    void reserve(Collection<ReserveSneakerDTO> reservedSneakers);

    void cancel(Collection<ReserveSneakerDTO> reservedSneakers);

    Double getSneakerPrices(Collection<Long> sneakerIds);

    List<Sneaker> findAllByIds(Collection<Long> sneakerIds);
}
