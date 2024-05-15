package vtiger.middleware.api.dto;

import lombok.Data;

@Data
public class LoginResultDto {
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private String time_zone;
    private String hour_format;
    private String date_format;
    private String is_admin;
    private String call_duration;
    private String other_event_duration;
    private String sessionName;
    private String userId;
    private String version;
    private String vtigerVersion;
}
