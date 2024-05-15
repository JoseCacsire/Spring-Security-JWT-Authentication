package com.example.SpringSecurityJwt.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email//tiene q insertar si o si un email
    @NotBlank//no vacio
    @Size(max = 80)
    private String email;
    @NotBlank
    @Size(max = 30)
    private String username;
    @NotBlank
    private String password;
    //(Creando la tabla intermedia)
    //CasadeType.Persist:Cuando ingrese un usuario en la BD que tambien cree el rol en la tabla roles,pero
    //si el usuario se elimina,q no me borre los roles.Ya q ese rol quizas necesite para otros usuaries

    //Eager:Necesito q cuando consulte el usuario me traiga todos los roles q estan ascociados a ese usuario
    // de una vez.Con el lazy me traeria uno por uno.

    //TargetEntity=RoleEntity: Con que entidad se va a establecer la relacion(opcional ponerlo)
    @ManyToMany(fetch = FetchType.EAGER,targetEntity = RoleEntity.class,cascade = CascadeType.PERSIST)
    @JoinTable(//Tabla intermedia
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;//set no permite elementos duplicados.El list si puede haber duplicados

}
