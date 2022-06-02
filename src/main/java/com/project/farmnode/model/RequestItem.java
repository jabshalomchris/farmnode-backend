package com.project.farmnode.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private double price;
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "requestId", referencedColumnName = "requestId")
    private Request request;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "produceId", referencedColumnName = "produceId")
    private Produce produce;
}
