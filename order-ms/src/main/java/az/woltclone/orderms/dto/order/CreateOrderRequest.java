package az.woltclone.orderms.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@Builder
public class CreateOrderRequest {
    @NotBlank
    private String deliveryAddress;

    @NotNull
    private Double deliveryLat;

    @NotNull
    private Double deliveryLng;

    @Valid
    @NotEmpty
    private List<OrderItemRequest> items;
}
