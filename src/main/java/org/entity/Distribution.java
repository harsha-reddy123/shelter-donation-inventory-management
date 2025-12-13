package org.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// distributions entity which is how much amount is going out
@Entity
@Table(name = "distributions")
public class Distribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "donation_type", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private DonationType donationType;


    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "distribution_date", nullable = false)
    private LocalDate distributionDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // who recieved the distribution or why did we made the distribution like that
    @Column(name = "recipient", length = 255)
    private String recipient;

    public Distribution() {
        // Default constructor required by JPA
    }

    public Distribution(DonationType donationType, BigDecimal quantity, LocalDate distributionDate) {
        this.donationType = donationType;
        this.quantity = quantity;
        this.distributionDate = distributionDate;
    }


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (distributionDate == null) {
            distributionDate = LocalDate.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DonationType getDonationType() {
        return donationType;
    }

    public void setDonationType(DonationType donationType) {
        this.donationType = donationType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDistributionDate() {
        return distributionDate;
    }

    public void setDistributionDate(LocalDate distributionDate) {
        this.distributionDate = distributionDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "Distribution{" +
                "id=" + id +
                ", donationType=" + donationType +
                ", quantity=" + quantity +
                ", distributionDate=" + distributionDate +
                ", recipient='" + recipient + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
