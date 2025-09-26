package com.unitime.unitime_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "promotions", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "field"})})
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "promotion_level")
    private PromotionLevel promotionLevel;

    @Column(name = "field")
    private String field;

    @Column(name = "access_code", unique = true, nullable = false)
    private String accessCode;

    @Column(name = "created_at" )
    private Timestamp createdAt;

}
