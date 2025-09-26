package com.unitime.unitime_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "appointment_visibility")
public class AppointmentVisibility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name ="group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Promotion promotion;

    @PrePersist
    @PreUpdate

    private void validateConstraint() {
        if((this.group != null && this.promotion != null) ||
                (this.group == null && this.promotion == null)) {
            throw new IllegalStateException("Either group or promotion must be set, but not both");
        }
    }

}
