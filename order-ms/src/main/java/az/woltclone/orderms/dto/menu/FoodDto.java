package az.woltclone.orderms.dto.menu;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class FoodDto {
    private Long id;

    private String name;

    private BigDecimal price;

    private Boolean available;

}
