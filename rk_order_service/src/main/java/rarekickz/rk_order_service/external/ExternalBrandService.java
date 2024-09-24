package rarekickz.rk_order_service.external;

import rarekickz.rk_order_service.dto.BrandDTO;

import java.util.Map;

public interface ExternalBrandService {

    Map<Long, BrandDTO> getAllBrands();
}
