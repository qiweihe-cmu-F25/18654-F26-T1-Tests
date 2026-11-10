package userprofile.controller;

import jakarta.annotation.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import userprofile.dto.UserProfileLoginRequest;
import userprofile.dto.UserProfileLoginResponse;
import userprofile.jwt.annotation.SkipToken;
import userprofile.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/userprofile")
public class AuthenticationController {

    @Resource
    private AuthenticationService authenticationService;

    @SkipToken
    @PostMapping("/login")
    public ResponseEntity<UserProfileLoginResponse> login(@RequestBody UserProfileLoginRequest userProfileLoginRequest) {
        UserProfileLoginResponse response = this.authenticationService.login(userProfileLoginRequest);
        
        // set jwt into response header
        String jwt = response.getJwt();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        
        return ResponseEntity.ok().headers(headers).body(response);
    }
}
