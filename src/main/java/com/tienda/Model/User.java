package com.tienda.Model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @Column(name = "name", nullable = false) // Personaliza el nombre de la columna y otras propiedades
    private String userName;

    @Column(name = "last_name" , nullable = false) // Personaliza el nombre de la columna
    private String userLastName;

    @Column(name = "dni", nullable = false,unique = true)
    private String userDni;

    @Column(name = "email", nullable = false,unique = true)
    private String userEmail;

    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255)")
    private String userPassword;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;



    // Relación Many-to-Many con la entidad Role, EAGER carga los roles cuando se carga un usuario.
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


}
