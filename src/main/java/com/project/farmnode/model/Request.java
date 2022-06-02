package com.project.farmnode.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;


@Entity
@Data

@AllArgsConstructor
@NoArgsConstructor
@Table(name="request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    private String requestStatus;
    private String message;
    @OneToMany(mappedBy = "request", fetch = LAZY)
    private List<RequestItem> requestItem;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "buyerId", referencedColumnName = "userId")
    private User buyerId;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "growerId", referencedColumnName = "userId")
    private User growerId;
    private Instant createdDate;

}
