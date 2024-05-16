package com.example.SpringSecurityJwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringSecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);
	}

//	@Autowired
//	PasswordEncoder passwordEncoder;
//
//	@Autowired
//	UserRepository userRepository;

	//crear tres usuarios inmediatamente la aplicacion se levante
//	@Bean
//	CommandLineRunner init(){
//		return args -> {
//			UserEntity userEntity = UserEntity.builder()
//					.email("jose@gmail.com")
//					.username("jose")
//					.password(passwordEncoder.encode("1234"))
//					.roles(Set.of(RoleEntity.builder()
//							.name(ERole.valueOf(ERole.ADMIN.name()))
//							.build()))
//					.build();
//
//			UserEntity userEntity2 = UserEntity.builder()
//					.email("luis@gmail.com")
//					.username("luis")
//					.password(passwordEncoder.encode("123"))
//					.roles(Set.of(RoleEntity.builder()
//							.name(ERole.valueOf(ERole.ADMIN.name()))
//							.build()))
//					.build();
//
//			UserEntity userEntity3 = UserEntity.builder()
//					.email("torres@gmail.com")
//					.username("torres")
//					.password(passwordEncoder.encode("12"))
//					.roles(Set.of(RoleEntity.builder()
//							.name(ERole.valueOf(ERole.ADMIN.name()))
//							.build()))
//					.build();
//			userRepository.save(userEntity);
//			userRepository.save(userEntity2);
//			userRepository.save(userEntity3);
//		};
//	}

}
