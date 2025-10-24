package com.unitime.unitime_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="title")
    private String title;

    @Column(name = "location")
    private String location;

    @Column(name="start_time")
    private Timestamp startTime;

    @Column(name="end_time")
    private Timestamp endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type")
    private AppointmentType appointmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_scope")
    private AppointmentScope appointmentScope;

    @Column(name = "recurrence_rule")
    private String recurrenceRule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by",referencedColumnName = "id",nullable = true)
    private User createdBy;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

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
