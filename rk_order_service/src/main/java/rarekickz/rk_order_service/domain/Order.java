package rarekickz.rk_order_service.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import rarekickz.rk_order_service.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Builder.Default
    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    private UUID uuid = UUID.randomUUID();

    @OneToOne
    @JoinColumn(name = "delivery_info_id", referencedColumnName = "id")
    private DeliveryInfo deliveryInfo;


    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private Set<OrderInventory> orderInventory;

    @CreatedDate
    private LocalDateTime createdAt;
}
