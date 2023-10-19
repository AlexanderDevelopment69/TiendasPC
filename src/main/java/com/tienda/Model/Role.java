package com.tienda.Model;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name = "roles")
public class Role {

    // Identificador único para cada rol.
    @Id
    // Generación automática del ID utilizando una estrategia de base de datos.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Nombre de la columna en la base de datos.
    @Column(name = "role_id")
    private Long roleId;

    // Nombre del rol (p. ej., "ADMINISTRATOR" o "USER").
    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

//    // Colección de usuarios asociados a este rol a través de una relación Many-to-Many.
//    @ManyToMany(mappedBy = "roles")
//    // Inicializa una colección vacía de usuarios.
//    private Set<User> users = new HashSet<>();



}
