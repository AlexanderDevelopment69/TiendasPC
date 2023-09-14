package com.tienda.Model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name", nullable = false) // Personaliza el nombre de la columna y otras propiedades
    private String name;

    @Column(name = "user_last_name" , nullable = false) // Personaliza el nombre de la columna
    private String lastName;

    @Column(name = "dni", nullable = false, columnDefinition = "VARCHAR(9)",unique = true)
    private String dni;

    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(255)",unique = true)
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255)")
    private String password;

//    // Relación Many-to-Many con la entidad Role, EAGER carga los roles cuando se carga un usuario.
//    @ManyToMany(fetch = FetchType.EAGER)
//    // Define la tabla intermedia para la relación Many-to-Many.
//    @JoinTable(
//            name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//
//    // Inicializa una colección vacía de roles.
//    private Set<Role> roles = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public User(String email, String password) {
        this.email=email;
        this.password=password;
    }

    public User(String email, String password, String dni, String lastNames, String names) {
        this.email=email;
        this.password=password;
        this.dni=dni;
        this.lastName=lastNames;
        this.name=names;
    }
}
