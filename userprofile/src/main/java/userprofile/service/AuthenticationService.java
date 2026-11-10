package userprofile.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import userprofile.domain.UserProfile;
import userprofile.dto.UserProfileLoginRequest;
import userprofile.dto.UserProfileLoginResponse;
import userprofile.exceptions.CustomException;
import userprofile.jwt.JwtUtils;
import userprofile.repository.UserProfileRepository;

@Service
public class AuthenticationService {

    private final JwtUtils jwtUtils;
    private final UserProfileRepository userProfileRepository;

    public AuthenticationService(JwtUtils jwtUtils, UserProfileRepository userProfileRepository) {
        this.jwtUtils = jwtUtils;
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfileLoginResponse login(UserProfileLoginRequest userProfileLoginRequest) {
        UserProfile profile = this.userProfileRepository.findByUsername(userProfileLoginRequest.getUsername());
        if(profile == null) {
            throw new CustomException("Cannot find user", 404);
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(passwordEncoder.matches(userProfileLoginRequest.getPassword(), profile.getPasswordHash())) {
            return new UserProfileLoginResponse(
                profile.getId(),
                profile.getUsername(),
                jwtUtils.createJwt(profile)
            );
        } else {
            throw new CustomException("Login failed", 401);
        }
    }

}
