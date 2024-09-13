package com.rarekickz.rk_inventory_service.external.impl;

import com.google.protobuf.Empty;
import com.rarekickz.proto.lib.OrderServiceGrpc;
import com.rarekickz.proto.lib.PopularSneakerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalOrderServiceImplUnitTest {

    @InjectMocks
    private ExternalOrderServiceImpl externalOrderService;

    @Mock
    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    @Test
    void findMostPopularSneakerId_returnsSneakerId() {
        // Arrange
        PopularSneakerResponse popularSneakerResponse = PopularSneakerResponse.newBuilder()
                .setSneakerId(1L)
                .build();
        when(orderServiceBlockingStub.findMostPopularSneaker(Empty.newBuilder().build())).thenReturn(popularSneakerResponse);

        // Act
        Long actualMostPopularSneakerId = externalOrderService.findMostPopularSneakerId();

        // Assert
        assertThat(actualMostPopularSneakerId, is(equalTo(1L)));
    }
}
