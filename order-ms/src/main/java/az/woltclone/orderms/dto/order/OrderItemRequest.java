package az.woltclone.orderms.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Builder
@Setter
@Getter
public class OrderItemRequest {
    @NotNull
    private Long foodId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
