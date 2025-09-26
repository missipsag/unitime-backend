package com.unitime.unitime_backend.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "groups" , uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "promotion_id"})})
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @OneToMany(mappedBy = "group")
    Set<User> users = new HashSet<>();

    @Column(name = "access_code", nullable = false, unique = true)
    private String accessCode;

    @Column(name = "created_at")
    private Timestamp createdAt;
}
