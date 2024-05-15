package vtiger.middleware.api.dto;

import lombok.Data;

@Data
public class ContactVtigerResponseDto {
    private boolean success;
    private ContactResultDto result;
}
