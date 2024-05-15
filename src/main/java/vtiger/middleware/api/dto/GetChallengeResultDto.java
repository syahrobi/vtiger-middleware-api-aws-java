package vtiger.middleware.api.dto;

import lombok.Data;

@Data
public class GetChallengeResultDto {
    private String token;
    private int serverTime;
    private int expireTime;

}
