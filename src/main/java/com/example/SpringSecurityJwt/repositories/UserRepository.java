package com.example.SpringSecurityJwt.repositories;

import com.example.SpringSecurityJwt.models.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long> {
    //Usando esto
    Optional<UserEntity> findByUsername(String username);//poniendo fyndBy ya jpa sabe q metodo aplicar
    @Query("select u from UserEntity u where u.username = ?1")
    Optional<UserEntity> getName(String username);



}
