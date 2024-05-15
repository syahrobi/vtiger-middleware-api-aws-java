package vtiger.middleware.api.dto;

import lombok.Data;

@Data
public class TicketRequestDto {
    private String title;
    private String desc;
    private String assignToId;
    private String contactId;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
}
