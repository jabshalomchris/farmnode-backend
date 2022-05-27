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
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    private String requestStatus;
    @OneToMany
    private List<RequestItem> RequestItem;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "buyerId", referencedColumnName = "userId")
    private User buyerId;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "growerId", referencedColumnName = "userId")
    private User growerId;
    private Instant createdDate;
}
