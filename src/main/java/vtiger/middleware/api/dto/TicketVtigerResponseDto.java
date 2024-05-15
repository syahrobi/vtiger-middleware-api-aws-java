package vtiger.middleware.api.dto;

import lombok.Data;

@Data
public class TicketVtigerResponseDto {
    private boolean success;
    private TicketResultDto result;
}
