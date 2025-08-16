package com.soa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

@Table(name="users")
@Entity
@Data
public class User {

    @Id
    @Column
    private Long id;
    @Column
    private String username;
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