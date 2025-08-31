package com.mycompany.authenticateservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // avoids conflicts with reserved "user"
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;  // for User or Admin
}
