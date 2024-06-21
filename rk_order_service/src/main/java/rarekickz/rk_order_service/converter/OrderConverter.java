package rarekickz.rk_order_service.converter;

import lombok.experimental.UtilityClass;
import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.dto.OrderDTO;
import rarekickz.rk_order_service.dto.OrderInventoryDTO;
import rarekickz.rk_order_service.dto.OrderPreviewDTO;

import java.util.List;

@UtilityClass
public class OrderConverter {

    public static List<OrderDTO> toOrderDTOList(final List<Order> orders) {
        return orders.stream()
                .map(OrderConverter::toOrderDTO)
                .toList();
    }

    public static OrderDTO toOrderDTO(final Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .email(order.getDeliveryInfo().getEmail())
                .address(order.getDeliveryInfo().getStreet())
                .build();
    }

    public static OrderPreviewDTO toOrderPreviewDTO(final Order order, final List<OrderInventoryDTO> orderInventoryDTOs) {
        return OrderPreviewDTO.builder()
                .orderStatus(order.getOrderStatus())
                .uuid(order.getOrderUuid().toString())
                .orderPrice(order.getTotalPrice())
                .orderInventory(orderInventoryDTOs)
                .build();
    }
}
