package az.woltclone.orderms.client;

import az.woltclone.orderms.dto.common.ResultDto;
import az.woltclone.orderms.dto.user.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8080")
public interface UserClient {
    @GetMapping("/api/users/couriers")
    ResultDto<List<UserResponse>> getAllCouriers();
}
