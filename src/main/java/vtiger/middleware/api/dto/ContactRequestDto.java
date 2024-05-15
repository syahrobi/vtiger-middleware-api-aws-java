package vtiger.middleware.api.dto;

import lombok.Data;

@Data
public class ContactRequestDto {
    private String firstname;
    private String lastname;
    private String assignedUserId;
    private String mobile;
    private String email;
}
