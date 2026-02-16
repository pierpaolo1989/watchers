package com.soa.model;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

@Table(name="users")
@Entity
@Data
public class User {

    @Id
    @Column
    @GeneratedValue
    private Long id;
    @Column
    private String email;
    @Column
    private String passwordSalt;
    @Column
    private String passwordHash;
    @Column
    private Role role;

    public boolean checkPassword(String password) {
        return DigestUtils.sha1Hex(password + passwordSalt).equals(passwordHash);
    }

}