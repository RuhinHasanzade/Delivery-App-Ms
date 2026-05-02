package az.woltclone.orderms.client;

import az.woltclone.orderms.dto.common.ResultDto;
import az.woltclone.orderms.dto.menu.FoodDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "menu-service",url = "${services.menu.url}")
public interface MenuClient {
    @GetMapping("/api/menu/foods/{id}")
    ResultDto<FoodDto> getFoodById(@PathVariable Long id);
}
