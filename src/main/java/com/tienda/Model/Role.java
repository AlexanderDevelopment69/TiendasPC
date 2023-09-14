package com.tienda.Model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

// Marca la clase como una entidad de JPA, que se mapeará a una tabla en la base de datos.
@Entity

// Lombok: Genera automáticamente getters, setters, toString, hashCode, y equals para los campos de la clase.
@Data

// Constructor sin argumentos.
@NoArgsConstructor

// Constructor con todos los argumentos.
@AllArgsConstructor

// Define el nombre de la tabla en la base de datos.
@Table(name = "roles")
public class Role {

    // Identificador único para cada rol.
    @Id

    // Generación automática del ID utilizando una estrategia de base de datos.
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // Nombre de la columna en la base de datos.
    @Column(name = "role_id")
    private Long id;

    // Nombre del rol (p. ej., "ADMINISTRATOR" o "USER").
    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

//    // Colección de usuarios asociados a este rol a través de una relación Many-to-Many.
//    @ManyToMany(mappedBy = "roles")
//    // Inicializa una colección vacía de usuarios.
//    private Set<User> users = new HashSet<>();
}
