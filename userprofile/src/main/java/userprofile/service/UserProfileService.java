package userprofile.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import userprofile.domain.UserProfile;
import userprofile.dto.*;
import userprofile.exceptions.CustomException;
import userprofile.exceptions.InvalidUserInputException;
import userprofile.gateway.ExpenseManagementGateway;
import userprofile.gateway.TripManagementGateway;
import userprofile.model.Expense;
import userprofile.model.Trip;
import userprofile.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Service layer contains the business logic of the application. It acts as an intermediary between the controller and the repository.
 *  Spring Boot annotations:
 *  {@code @Service} annotation is used at class level. It tells the Spring that class contains the business logic.
 */
@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final ExpenseManagementGateway expenseManagementGateway;
    private final TripManagementGateway tripManagementGateway;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
        this.tripManagementGateway = new TripManagementGateway();
        tripManagementGateway.init();
        this.expenseManagementGateway = new ExpenseManagementGateway();
        expenseManagementGateway.init();
    }

    public UserProfileResponse getUserProfileById(Long id) {
        UserProfile profile = this.userProfileRepository.findById(id).orElseThrow();
        Map<String, String> params = new HashMap<>();
        params.put("userId", id.toString());

        // NOTE: Trips do not store User ID, yet. So this requests will always return an exception / null.
        List<Trip> trips = null;
        try{
            TripRequest tripRequest = new TripRequest(id);
            trips = this.tripManagementGateway.getTrips(tripRequest).block().getTrips();
        } catch(Exception e) {
            // Do nothing
        }
        // NOTE: Expenses do not store User ID, yet. So this requests will always return an exception / null.
        List<Expense> expenses = null;
        try{
            ExpenseRequest expenseRequest = new ExpenseRequest(id);
            expenses = this.expenseManagementGateway.getExpenses(expenseRequest).block().getExpenses();
        } catch(Exception e) {
            // Do nothing
        }

        UserProfileResponse response = new UserProfileResponse(
                profile.getId(),
                profile.getUsername(),
                profile.getEmail(),
                trips,
                expenses
        );

        return response;
    }

    @Transactional
    public UserProfileCreateResponse createNewUser(UserProfileCreateRequest userProfileCreateRequest) throws CustomException {
        UserProfile newUser = null;
        if (userProfileRepository.findByUsername(userProfileCreateRequest.getUsername()) != null) {
            throw new CustomException("Username already exists", 409);
        }
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(userProfileCreateRequest.getPassword());
            newUser = new UserProfile(
                userProfileCreateRequest.getUsername(),
                userProfileCreateRequest.getEmail(),
                hashedPassword
            );
            userProfileRepository.save(newUser);
        } catch (Exception e) {
            throw new InvalidUserInputException("Invalid request.");
        }
        return new UserProfileCreateResponse("Success", newUser.getId());
    }
}
