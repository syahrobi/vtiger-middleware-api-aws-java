package vtiger.middleware.api.dto;

import lombok.Data;

@Data
public class LoginVtigerResponseDto {
    private boolean success;
    private LoginResultDto result;
}
