package com.project.farmnode.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produce {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long produceId;
    private String produceName;
    private String description;
    private boolean produce_status;
    private double price;
    //category
    private String category;
    //measurment
    private String address;
    //geolocation
    private double longitude;
    private double latitude;
    private boolean publish_status;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;



}
