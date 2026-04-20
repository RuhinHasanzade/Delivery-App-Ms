package az.woltclone.orderms.dto.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto<T> {
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
}