package com.soa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "watch_details")
@Entity
@Data
public class WatchDetail {

    @Id
    private Long id;
}
