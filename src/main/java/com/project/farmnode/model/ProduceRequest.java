package com.project.farmnode.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.LAZY;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requestId;
    private String status;
    @OneToMany
    List<ProduceRequestItem> produceRequestItems;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    private Instant createdDate;

}
