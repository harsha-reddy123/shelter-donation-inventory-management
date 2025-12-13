package org.dto;


import org.entity.DonationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Used when returning donation data to clients
public class DonationResponse {

    private Long id;
    private String donorName;
    private DonationType donationType;
    private BigDecimal quantity;
    private LocalDate donationDate;
    private LocalDateTime createdAt;

    // constructors

    public DonationResponse() {
    }

    public DonationResponse(Long id, String donorName, DonationType donationType,
                            BigDecimal quantity, LocalDate donationDate, LocalDateTime createdAt) {
        this.id = id;
        this.donorName = donorName;
        this.donationType = donationType;
        this.quantity = quantity;
        this.donationDate = donationDate;
        this.createdAt = createdAt;
    }

    // getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
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

    public LocalDate getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(LocalDate donationDate) {
        this.donationDate = donationDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "DonationResponse{" +
                "id=" + id +
                ", donorName='" + donorName + '\'' +
                ", donationType=" + donationType +
                ", quantity=" + quantity +
                ", donationDate=" + donationDate +
                ", createdAt=" + createdAt +
                '}';
    }
}