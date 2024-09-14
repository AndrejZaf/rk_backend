package com.rarekickz.rk_payment_service.external.impl;

import com.rarekickz.proto.lib.CustomerDetailsResponse;
import com.rarekickz.proto.lib.OrderRequest;
import com.rarekickz.proto.lib.OrderResponse;
import com.rarekickz.proto.lib.OrderServiceGrpc;
import com.rarekickz.proto.lib.Product;
import com.rarekickz.proto.lib.SelectedProductsResponse;
import com.rarekickz.rk_payment_service.dto.OrderDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.rarekickz.rk_payment_service.external.converter.OrderDetailsConverter.convertToOrderDetailsDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalOrderServiceImplUnitTest {

    @InjectMocks
    private ExternalOrderServiceImpl externalOrderService;

    @Mock
    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    private OrderRequest orderRequest;
    private String orderId;

    @BeforeEach
    void setup() {
        orderId = "orderId";
        orderRequest = OrderRequest.newBuilder()
                .setOrderId(orderId)
                .build();
    }

    @Test
    void getOrderDetails_returnsOrderDetails() {
        // Arrange
        OrderResponse orderResponse = OrderResponse.newBuilder()
                .setCustomerDetailsResponse(CustomerDetailsResponse.newBuilder()
                        .setEmail("test")
                        .setName("test").build())
                .setSelectedProductResponse(SelectedProductsResponse.newBuilder()
                        .addProducts(Product.newBuilder()
                                .setId(1L)
                                .setName("Test")
                                .setPrice(100.0)
                                .build())
                        .build())
                .build();
        OrderDetailsDTO orderDetailsDTO = convertToOrderDetailsDTO(orderResponse);
        when(orderServiceBlockingStub.getOrderDetails(orderRequest)).thenReturn(orderResponse);

        // Act
        OrderDetailsDTO actualOrderDetails = externalOrderService.getOrderDetails(orderId);

        // Assert
        assertThat(actualOrderDetails, is(equalTo(orderDetailsDTO)));
    }

    @Test
    void finalizeOrder_sendsFinalizeOrderRequest() {
        // Arrange
        // Act
        externalOrderService.finalizeOrder(orderId);

        // Assert
        verify(orderServiceBlockingStub).finalizeOrder(orderRequest);
    }

    @Test
    void cancelOrder_sendsCancelOrderRequest() {
        // Arrange
        // Act
        externalOrderService.cancelOrder(orderId);

        // Assert
        verify(orderServiceBlockingStub).cancelOrder(orderRequest);
    }
}
