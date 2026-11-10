package userprofile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
public class UserProfileCreateResponse {
    private String message;

    private Long userId;

    public UserProfileCreateResponse(String message) {
        this.message = message;
    }
}
