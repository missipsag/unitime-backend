package com.unitime.unitime_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "promotions",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"name", "field"}
        )})
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private PromotionLevel promotionLevel;

    @Column(name = "field")
    private String field;

    @Column(name = "access_code", unique = true, nullable = false)
    private String accessCode;

    @OneToMany(mappedBy = "promotion")
    private List<Group> groups ;

    @OneToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "created_at" )
    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Timestamp.from(Instant.now());
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Timestamp.from(Instant.now());
    }
}
