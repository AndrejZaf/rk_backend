package rarekickz.rk_order_service.external.impl;

import com.google.protobuf.Empty;
import com.rarekickz.proto.lib.Brand;
import com.rarekickz.proto.lib.BrandServiceGrpc;
import com.rarekickz.proto.lib.BrandsResponse;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import rarekickz.rk_order_service.dto.BrandDTO;
import rarekickz.rk_order_service.external.ExternalBrandService;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExternalBrandServiceImpl implements ExternalBrandService {

    @GrpcClient("brandsService")
    private BrandServiceGrpc.BrandServiceBlockingStub brandServiceBlockingStub;

    @Override
    public Map<Long, BrandDTO> getAllBrands() {
        final BrandsResponse brandsResponse = brandServiceBlockingStub.getAllBrands(Empty.getDefaultInstance());
        return brandsResponse.getBrandsList().stream()
                .collect(Collectors.toMap(Brand::getId, brand -> new BrandDTO(brand.getId(), brand.getName())));
    }
}
