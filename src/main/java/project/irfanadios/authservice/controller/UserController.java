package project.irfanadios.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.irfanadios.authservice.request.SignInRequest;
import project.irfanadios.authservice.request.SignUpRequest;
import project.irfanadios.authservice.response.SignInResponse;
import project.irfanadios.authservice.service.usermanagement.UserService;
import project.irfanadios.authservice.util.response.DataResponseBuilder;

@RestController
@RequestMapping("/v1/user-management")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("user/sign-in")
    public ResponseEntity<DataResponseBuilder<SignInResponse>> userSignIn(@Validated @RequestBody SignInRequest request) {
        DataResponseBuilder<SignInResponse> response = userService.signIn(request);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/user")
    public ResponseEntity<DataResponseBuilder<Object>> userSignUp(@Validated @RequestBody SignUpRequest request) {
        DataResponseBuilder<Object> response = userService.signUp(request);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
