package com.rarekickz.rk_inventory_service.external.converter;

import com.rarekickz.proto.lib.ReserveSneakersRequest;
import com.rarekickz.proto.lib.SneakerRequest;
import com.rarekickz.rk_inventory_service.dto.ReserveSneakerDTO;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ExternalSneakerConverter {

    public static List<ReserveSneakerDTO> convertToReserveSneakerDTOs(ReserveSneakersRequest reserveSneakersRequest) {
        return reserveSneakersRequest.getSneakersList().stream()
                .map(ExternalSneakerConverter::convertToReserveSneakerDTO)
                .toList();
    }

    public static ReserveSneakerDTO convertToReserveSneakerDTO(SneakerRequest sneakerRequest) {
        return new ReserveSneakerDTO(sneakerRequest.getSneakerId(), sneakerRequest.getSneakerSize());
    }
}
