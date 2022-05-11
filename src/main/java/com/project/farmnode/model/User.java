package com.project.farmnode.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String name;
    private String username;
    private String password;
    //private String email;
    /*@ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();*/
    private Instant created;
    private boolean enabled;

}
