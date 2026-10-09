package com.auth.graphqlauth.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.auth.graphqlauth.entities.UserInfoEntity;
import org.springframework.boot.security.autoconfigure.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfoEntity,Long> {
    Optional<UserInfoEntity> findByEmailId(String emailId);
}