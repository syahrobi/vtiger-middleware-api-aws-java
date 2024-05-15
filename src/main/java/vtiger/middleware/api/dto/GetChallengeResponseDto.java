package vtiger.middleware.api.dto;

import lombok.Data;

@Data
public class GetChallengeResponseDto {
    private boolean success;
    private GetChallengeResultDto result;
}
