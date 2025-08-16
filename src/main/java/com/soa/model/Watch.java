package com.soa.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Table(name = "watches")
@Entity
@Data
public class Watch {

    @Id
    private Long id;

    @Column(name = "reference_id")
    private String referenceId;

    @ManyToOne
    @JoinColumn(name="producer_id", nullable=false)
    private Producer producer;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @OneToOne
    private WatchDetail specifications;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

}
