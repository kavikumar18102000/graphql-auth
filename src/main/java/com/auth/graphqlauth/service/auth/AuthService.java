package com.auth.graphqlauth.service.auth;

import com.auth.graphqlauth.config.jwtConfig.JwtTokenGenerator;
import com.auth.graphqlauth.dto.AuthResponseDto;
import com.auth.graphqlauth.repository.UserInfoRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class AuthService {

    private final UserInfoRepo userInfoRepo;
    private final JwtTokenGenerator jwtTokenGenerator;

    public AuthService(UserInfoRepo userInfoRepo, JwtTokenGenerator jwtTokenGenerator) {
        this.userInfoRepo = userInfoRepo;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    public AuthResponseDto getJwtTokensAfterAuthentication() {
        try
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            /*var userInfoEntity = userInfoRepo.findByEmailId(authentication.getName())
                    .orElseThrow(()->{
                        log.error("[AuthService:userSignInAuth] User :{} not found",authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,"USER NOT FOUND ");});*/


            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated",authentication.getName());
            return  AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(authentication.getName())
                    .tokenType(TokenType.BEARER)
                    .build();


        }catch (Exception e){
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please Try Again");
        }
    }

}
