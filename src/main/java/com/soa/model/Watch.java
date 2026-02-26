package com.soa.model;

import com.soa.model.enums.MovementEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Table(name = "watches")
@Entity
@Data
public class Watch {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "model_name")
    private String model;

    @Column(name = "movement")
    private MovementEnum movement;

    @Column(name = "reference_id")
    private String referenceId;

    @ManyToOne
    @JoinColumn(name = "producer_id", nullable=false)
    private Producer producer;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable=false)
    private User user;

    @OneToOne
    private WatchDetail specifications;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

}
