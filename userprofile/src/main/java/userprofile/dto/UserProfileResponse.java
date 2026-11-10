package userprofile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import userprofile.model.Expense;
import userprofile.model.Trip;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Long id;

    private String username;

    private String email;

    private List<Trip> trips;

    private List<Expense> expenses;

}
