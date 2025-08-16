package com.soa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "producers")
@Entity
@Data
public class Producer {
    @Id
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private String code;
}
