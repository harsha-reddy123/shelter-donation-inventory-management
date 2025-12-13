package org.dto;

import org.entity.DonationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Used when returning distribution data to clients
public class DistributionResponse {

    private Long id;
    private DonationType donationType;
    private BigDecimal quantity;
    private LocalDate distributionDate;
    private String recipient;
    private LocalDateTime createdAt;

    // constructors

    public DistributionResponse() {
    }

    public DistributionResponse(Long id, DonationType donationType, BigDecimal quantity,
                                LocalDate distributionDate, String recipient, LocalDateTime createdAt) {
        this.id = id;
        this.donationType = donationType;
        this.quantity = quantity;
        this.distributionDate = distributionDate;
        this.recipient = recipient;
        this.createdAt = createdAt;
    }

    // getters & setters

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

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "DistributionResponse{" +
                "id=" + id +
                ", donationType=" + donationType +
                ", quantity=" + quantity +
                ", distributionDate=" + distributionDate +
                ", recipient='" + recipient + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}