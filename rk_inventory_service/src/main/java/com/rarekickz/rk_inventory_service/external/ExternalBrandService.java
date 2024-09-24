package com.rarekickz.rk_inventory_service.external;

import com.google.protobuf.Empty;
import com.rarekickz.proto.lib.Brand;
import com.rarekickz.proto.lib.BrandServiceGrpc;
import com.rarekickz.proto.lib.BrandsResponse;
import com.rarekickz.rk_inventory_service.service.BrandService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class ExternalBrandService extends BrandServiceGrpc.BrandServiceImplBase {

    private final BrandService brandService;

    @Override
    public void getAllBrands(final Empty request, final StreamObserver<BrandsResponse> responseObserver) {
        log.debug("Received request to get all brands");
        final List<Brand> brands = brandService.findAll().stream()
                .map(brand -> Brand.newBuilder()
                        .setId(brand.getId())
                        .setName(brand.getName())
                        .build())
                .toList();
        final BrandsResponse brandsResponse = BrandsResponse.newBuilder()
                .addAllBrands(brands)
                .build();
        responseObserver.onNext(brandsResponse);
        responseObserver.onCompleted();
    }
}
