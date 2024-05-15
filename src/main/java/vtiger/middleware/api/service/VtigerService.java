package vtiger.middleware.api.service;


import vtiger.middleware.api.dto.TicketRequestDto;
import vtiger.middleware.api.dto.TicketResponseDto;
import vtiger.middleware.api.dto.UserListResponseDto;

public interface VtigerService {
    TicketResponseDto create(TicketRequestDto ticketRequestDto);
    String resetSession();
    UserListResponseDto getUserList();
    String getToken();
}
