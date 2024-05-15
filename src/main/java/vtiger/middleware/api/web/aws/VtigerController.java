package vtiger.middleware.api.web.aws;

import vtiger.middleware.api.dto.TicketRequestDto;
import vtiger.middleware.api.dto.TicketResponseDto;
import vtiger.middleware.api.dto.UserListResponseDto;
import vtiger.middleware.api.service.VtigerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class VtigerController {

    @Autowired
    private VtigerService vtigerService;

    @Bean
    public Function<TicketRequestDto, TicketResponseDto> create() {
        return (TicketRequestDto) -> vtigerService.create(TicketRequestDto);
    }

    @Bean
    public Supplier<UserListResponseDto> userList() {
        return () -> vtigerService.getUserList();
    }

    @Bean
    public Supplier<String> getToken() {
        return () -> vtigerService.getToken();
    }

}
