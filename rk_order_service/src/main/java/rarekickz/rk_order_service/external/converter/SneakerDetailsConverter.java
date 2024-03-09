package rarekickz.rk_order_service.external.converter;

import com.rarekickz.proto.lib.SneakerDetails;
import lombok.experimental.UtilityClass;
import rarekickz.rk_order_service.dto.ExtendedSneakerDTO;

import java.util.List;

@UtilityClass
public class SneakerDetailsConverter {

    public static List<ExtendedSneakerDTO> convertToExtendedSneakerDTOList(List<SneakerDetails> sneakerDetails) {
        return sneakerDetails.stream()
                .map(SneakerDetailsConverter::convertToExtendedSneakerDTO)
                .toList();
    }

    private static ExtendedSneakerDTO convertToExtendedSneakerDTO(SneakerDetails sneakerDetails) {
        return new ExtendedSneakerDTO(sneakerDetails.getId(), sneakerDetails.getName(), sneakerDetails.getPrice());
    }
}
