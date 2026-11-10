package userprofile.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import userprofile.dto.UserProfileCreateRequest;
import userprofile.dto.UserProfileCreateResponse;
import userprofile.dto.UserProfileResponse;
import userprofile.exceptions.CustomException;
import userprofile.jwt.annotation.SkipToken;
import userprofile.service.UserProfileService;
import org.springframework.http.ResponseEntity;

/** Controller handles HTTP requests, maps them to the appropriate service methods, and returns responses.
 *  Spring Boot annotations:
 *  {@code @Controller}: is a class-level annotation. It marks a class as a web request handler. By default, it returns a string that indicates which route to redirect.
 *  It is mostly used with {@code @RequestMapping annotation. @RestController}: can be considered as a combination of {@code @Controller and @ResponseBody} (which binds the method return value to the response body) annotations.
 *  The {@code @RestController} annotation eliminates the need for annotating each method with {@code @ResponseBody}
 */

@RestController
@RequestMapping("/api/v1/userprofile")
public class UserProfileController {

    @Resource
    private UserProfileService userProfileService;

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserProfileById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(this.userProfileService.getUserProfileById(id));
        } catch (Exception e) {
            throw new CustomException("User not found", 404);
        }
    }

    @SkipToken
    @PostMapping
    public ResponseEntity<UserProfileCreateResponse> createUser(@RequestBody UserProfileCreateRequest userProfileCreateRequest) {
        UserProfileCreateResponse response = this.userProfileService.createNewUser(userProfileCreateRequest);
        return ResponseEntity.ok(response);
    }
}
