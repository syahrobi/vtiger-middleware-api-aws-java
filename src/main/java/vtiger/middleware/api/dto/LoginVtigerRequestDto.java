package vtiger.middleware.api.dto;

import lombok.Data;

@Data
public class LoginVtigerRequestDto {
    private String username;
    private String  accessKey;
}
