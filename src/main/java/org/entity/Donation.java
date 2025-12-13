package org.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// donations entity - who donated to make an entry in our db
@Entity
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // name of the donor
    @Column(name = "donor_name", nullable = false, length = 255)
    private String donorName;

    // type of donation made : money, food, clothing through enum i have setup 10 types right now
    @Column(name = "donation_type", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private DonationType donationType;

    /**
     * For money: representing dollar amount (500, 1050.50)
     * For items: represents quantity (50=50 cans of food/blankets)
     */
    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    // date of the donation made
    @Column(name = "donation_date", nullable = false)
    private LocalDate donationDate;

    // create this donor at in our db
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ========== Constructors ==========

    public Donation() {
        // Default constructor required by JPA
    }

    public Donation(String donorName, DonationType donationType, BigDecimal quantity, LocalDate donationDate) {
        this.donorName = donorName;
        this.donationType = donationType;
        this.quantity = quantity;
        this.donationDate = donationDate;
    }


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (donationDate == null) {
            donationDate = LocalDate.now();
        }
    }


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
        return "Donation{" +
                "id=" + id +
                ", donorName='" + donorName + '\'' +
                ", donationType=" + donationType +
                ", quantity=" + quantity +
                ", donationDate=" + donationDate +
                ", createdAt=" + createdAt +
                '}';
    }
}
