package com.auth.graphqlauth.controller;

import com.auth.graphqlauth.dto.AuthRequestDto;
import com.auth.graphqlauth.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign-in")
@RequiredArgsConstructor
class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    ResponseEntity<?> authenticateUser(@RequestBody AuthRequestDto requestDto){
        //Custom Authentication, instead of spring default
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.userName(), requestDto.password()));
        return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication());
    }
}