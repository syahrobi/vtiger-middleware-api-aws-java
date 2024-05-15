package vtiger.middleware.api.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class UserListResponseDto {
    private boolean success;
    private ArrayList<UserDto> result;
}
