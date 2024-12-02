package com.example.demo.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private Integer estimatedHours;  // Nullable

    private Integer completedHours;

    private Integer remainingEffort;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Calculating estimation accuracy
    public String getEstimationAccuracy() {
        if (estimatedHours == null || estimatedHours == 0) {
            return "NOT_ESTIMATED";
        }

        if (completedHours != null) {
            if (completedHours >= 0.9 * estimatedHours && completedHours <= 1.1 * estimatedHours) {
                return "ACCURATELY_ESTIMATED";
            } else if (completedHours < 0.9 * estimatedHours) {
                return "OVER_ESTIMATED";
            } else {
                return "UNDER_ESTIMATED";
            }
        }

        return "NOT_ESTIMATED";
    }

    // Update remaining effort based on estimated and completed hours
    public void updateRemainingEffort() {
        if (estimatedHours != null && estimatedHours >= 0 && completedHours != null && completedHours >= 0) {
            this.remainingEffort = estimatedHours - completedHours;
        } else {
            this.remainingEffort = null;
        }
    }

    public void setEstimatedHours(Integer estimatedHours) {
        if (estimatedHours != null && estimatedHours >= 0) {
            this.estimatedHours = estimatedHours;
        } else {
            this.estimatedHours = null;
        }
        updateRemainingEffort();
    }


    public void setCompletedHours(Integer completedHours) {
        if (completedHours != null && completedHours >= 0) {
            this.completedHours = completedHours;
        } else {
            this.completedHours = null;
            updateRemainingEffort();
        }
    }
}