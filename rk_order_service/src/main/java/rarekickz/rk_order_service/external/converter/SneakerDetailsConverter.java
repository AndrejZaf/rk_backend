package rarekickz.rk_order_service.external.converter;

import com.rarekickz.proto.lib.ExtendedSneakerDetails;
import com.rarekickz.proto.lib.SneakerDetails;
import lombok.experimental.UtilityClass;
import rarekickz.rk_order_service.dto.ExtendedSneakerDTO;
import rarekickz.rk_order_service.dto.ExtendedSneakerDetailsDTO;

import java.util.List;

@UtilityClass
public class SneakerDetailsConverter {

    public static List<ExtendedSneakerDTO> convertToExtendedSneakerDTOList(final List<SneakerDetails> sneakerDetails) {
        return sneakerDetails.stream()
                .map(SneakerDetailsConverter::convertToExtendedSneakerDTO)
                .toList();
    }

    public static List<ExtendedSneakerDetailsDTO> convertToExtendedSneakerDetailsDTOList(final List<ExtendedSneakerDetails> sneakerDetails) {
        return sneakerDetails.stream()
                .map(SneakerDetailsConverter::convertToExtendedSneakerDetailsDTO)
                .toList();
    }

    private static ExtendedSneakerDTO convertToExtendedSneakerDTO(final SneakerDetails sneakerDetails) {
        return new ExtendedSneakerDTO(sneakerDetails.getId(), sneakerDetails.getName(), sneakerDetails.getPrice());
    }

    private static ExtendedSneakerDetailsDTO convertToExtendedSneakerDetailsDTO(final ExtendedSneakerDetails sneakerDetails) {
        return new ExtendedSneakerDetailsDTO(sneakerDetails.getId(), sneakerDetails.getName(), sneakerDetails.getPrice(), sneakerDetails.getBrandName());
    }
}
