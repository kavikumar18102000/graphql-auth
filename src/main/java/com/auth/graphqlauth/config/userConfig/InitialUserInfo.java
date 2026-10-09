package com.auth.graphqlauth.config.userConfig;

import com.auth.graphqlauth.entities.UserInfoEntity;
import com.auth.graphqlauth.repository.UserInfoRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitialUserInfo implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepo userInfoRepo;

    @Override
    public void run(String... args) throws Exception {
        UserInfoEntity admin = new UserInfoEntity();
        admin.setUserName("admin");
        admin.setEmailId("admin@gmail.com");
        admin.setRoles("ADMIN");
        admin.setPassword(passwordEncoder.encode("root"));

        UserInfoEntity manager = new UserInfoEntity();
        manager.setUserName("manager");
        manager.setEmailId("manager@gmail.com");
        manager.setRoles("MANAGER");
        manager.setPassword(passwordEncoder.encode("manager"));

        UserInfoEntity user = new UserInfoEntity();
        user.setUserName("User");
        user.setEmailId("user@user.com");
        user.setRoles("USER");
        user.setPassword(passwordEncoder.encode("user"));

        userInfoRepo.saveAll(List.of(admin, manager, user));
    }
}
