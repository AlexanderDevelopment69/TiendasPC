package com.tienda.Model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Data
@Table(name = "userRoleChange")
public class UserRoleChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long change_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private String action; // "asignar" o "revocar"
    private LocalDateTime timestamp;

    // Getters y setters
}
