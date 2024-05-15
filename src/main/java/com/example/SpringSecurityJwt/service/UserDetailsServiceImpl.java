package com.example.SpringSecurityJwt.service;

import com.example.SpringSecurityJwt.models.UserEntity;
import com.example.SpringSecurityJwt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //buscando el usuario q se va a autenticar lo tiene q buscar en la BD
    @Override//Lo consulta por debajo spring security
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //recuperando el usuario de nuestra BD
        UserEntity userEntity = userRepository.findByUsername(username)
                                                   .orElseThrow(() -> new UsernameNotFoundException("El usuario "+username+" no existe."));
        /*
        Una vez que se ha encontrado el usuario, se obtienen los roles asociados a ese usuario desde la entidad UserEntity.
        Se asume que los roles están representados como enums, ya que se utiliza getName().name() para obtener el nombre del
        enum asociado a cada rol.
         */
        //getName().name() se está utilizando para obtener el nombre del enum asociado a un rol y luego concatenar la cadena "ROLE_" a ese nombre.
        //obteniendo los permisos
        Collection<? extends GrantedAuthority> authorities = userEntity.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName().name())))
                .collect(Collectors.toSet());

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}
