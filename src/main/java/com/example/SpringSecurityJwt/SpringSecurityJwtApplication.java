package com.example.SpringSecurityJwt;

import com.example.SpringSecurityJwt.models.ERole;
import com.example.SpringSecurityJwt.models.RoleEntity;
import com.example.SpringSecurityJwt.models.UserEntity;
import com.example.SpringSecurityJwt.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;


@SpringBootApplication
public class SpringSecurityJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        return args -> {

//          Create users
            UserEntity user1 = UserEntity.builder()
                    .email("admin@gmail.com")
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .estado(true)
                    .roles(Set.of(RoleEntity.builder()
                            .name(ERole.ADMIN)
                            .build()))
                    .build();

              UserEntity user2 = UserEntity.builder()
                    .email("user@gmail.com")
                    .username("user")
                    .password(passwordEncoder.encode("user"))
                    .estado(true)
                    .roles(Set.of(RoleEntity.builder()
                            .name(ERole.USER)
                            .build()))
                    .build();

            userEntityRepository.saveAll(List.of(user1,user2));
        };
    }

}
