package com.example.SpringSecurityJwt.service;

import com.example.SpringSecurityJwt.models.ERole;
import com.example.SpringSecurityJwt.models.RoleEntity;
import com.example.SpringSecurityJwt.models.UserEntity;
import com.example.SpringSecurityJwt.repositories.RoleRepository;
import com.example.SpringSecurityJwt.repositories.UserRepository;
import com.example.SpringSecurityJwt.request.CreateUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public UserEntity createUser(CreateUserDTO createUserDTO) {
        log.error("Impl");
        // Obtener los nombres de los roles del DTO
        Set<String> roleNames = createUserDTO.getRoles();

        // Convertir los nombres de los roles en entidades RoleEntity y agregarlos al usuario
        Set<RoleEntity> roles = roleNames.stream()
                .map(roleName -> {
                    try {
                        ERole role = ERole.valueOf(roleName);
                        RoleEntity existingRole = roleRepository.findByName(role);
                        if (existingRole != null) {
                            return existingRole;
                        } else {
                            return RoleEntity.builder().name(role).build();
                        }
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("El rol '" + roleName + "' no es válido");
                    }
                }).collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .email(createUserDTO.getEmail())
                .username(createUserDTO.getUsername())
                .password(createUserDTO.getPassword())
                .roles(roles)
                .build();

        userRepository.save(userEntity);

        return userEntity;

    }

    @Override
    public void deleteById(long l) {

    }
}
