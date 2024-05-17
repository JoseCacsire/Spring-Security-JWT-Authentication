package com.example.SpringSecurityJwt.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "role_id")
    private Long idRol;

    @Enumerated(EnumType.STRING)
    private ERole name;

}
