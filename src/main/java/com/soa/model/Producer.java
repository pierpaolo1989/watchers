package com.soa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "producers")
@Entity
@Data
public class Producer {
    private Long id;
    private String name;
    private String code;
}
