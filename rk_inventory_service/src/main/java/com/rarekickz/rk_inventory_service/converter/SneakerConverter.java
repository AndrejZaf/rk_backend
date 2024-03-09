package com.rarekickz.rk_inventory_service.converter;

import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.domain.SneakerImage;
import com.rarekickz.rk_inventory_service.domain.SneakerSize;
import com.rarekickz.rk_inventory_service.dto.SneakerCartDTO;
import com.rarekickz.rk_inventory_service.dto.SneakerDTO;
import com.rarekickz.rk_inventory_service.dto.SneakerSizeDTO;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@UtilityClass
public class SneakerConverter {

    public static SneakerDTO convertPremiumSneaker(final Sneaker sneaker) {
        return SneakerDTO.builder()
                .id(sneaker.getId())
                .description(sneaker.getDescription())
                .name(sneaker.getName())
                .images(convertToStringImages(sneaker.getSneakerImages()))
                .build();
    }

    public static SneakerDTO convertToSneakerDTO(final Sneaker sneaker) {
        return SneakerDTO.builder()
                .id(sneaker.getId())
                .brandId(sneaker.getBrand().getId())
                .brand(sneaker.getBrand().getName())
                .description(sneaker.getDescription())
                .gender(sneaker.getGender().ordinal())
                .price(sneaker.getPrice())
                .name(sneaker.getName())
                .images(convertToStringImages(sneaker.getSneakerImages()))
                .sizes(convertToSneakerSizeDTO(sneaker.getSneakerSizes()))
                .special(sneaker.isSpecial())
                .build();
    }

    public static List<SneakerCartDTO> convertToSneakerCartDTOs(final List<Sneaker> sneakers) {
        return sneakers.stream()
                .map(SneakerConverter::convertToSneakerCartDTO)
                .toList();
    }

    public static SneakerCartDTO convertToSneakerCartDTO(final Sneaker sneaker) {
        return SneakerCartDTO.builder()
                .id(sneaker.getId())
                .price(sneaker.getPrice())
                .name(sneaker.getName())
                .images(convertToStringImages(sneaker.getSneakerImages()))
                .build();
    }

    public static List<SneakerDTO> convertToSneakerDTOList(final Collection<Sneaker> sneakers) {
        return sneakers.stream()
                .map(SneakerConverter::convertToSneakerDTO)
                .toList();
    }

    private static List<String> convertToStringImages(final Set<SneakerImage> sneakerImages) {
        return sneakerImages.stream()
                .sorted(Comparator.comparing(SneakerImage::getId))
                .map(SneakerImage::getImageData)
                .toList();
    }

    private static List<SneakerSizeDTO> convertToSneakerSizeDTO(final Collection<SneakerSize> sneakerSizes) {
        return sneakerSizes.stream()
                .map(sneakerSize ->
                        new SneakerSizeDTO(sneakerSize.getSneakerSizeId().getSize(), sneakerSize.getQuantity()))
                .toList();
    }
}
