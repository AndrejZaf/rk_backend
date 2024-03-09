package com.rarekickz.rk_payment_service.external.converter;

import com.rarekickz.proto.lib.CustomerDetailsResponse;
import com.rarekickz.proto.lib.OrderResponse;
import com.rarekickz.proto.lib.SelectedProductsResponse;
import com.rarekickz.rk_payment_service.dto.CustomerDetailsDTO;
import com.rarekickz.rk_payment_service.dto.OrderDetailsDTO;
import com.rarekickz.rk_payment_service.dto.ProductDTO;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class OrderDetailsConverter {

    public static OrderDetailsDTO convertToOrderDetailsDTO(OrderResponse orderResponse) {
        return OrderDetailsDTO.builder()
                .customerDetails(convertToCustomerDetailsDTO(orderResponse.getCustomerDetailsResponse()))
                .products(convertToProductDTOList(orderResponse.getSelectedProductResponse()))
                .build();
    }

    private CustomerDetailsDTO convertToCustomerDetailsDTO(CustomerDetailsResponse customerDetailsResponse) {
        return CustomerDetailsDTO.builder()
                .email(customerDetailsResponse.getEmail())
                .name(customerDetailsResponse.getName())
                .build();
    }

    private List<ProductDTO> convertToProductDTOList(SelectedProductsResponse selectedProductsResponse) {
        return selectedProductsResponse.getProductsList().stream()
                .map(product -> ProductDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .build())
                .toList();
    }
}
