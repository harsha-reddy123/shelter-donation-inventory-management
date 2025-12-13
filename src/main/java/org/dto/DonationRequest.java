package org.dto;

import jakarta.validation.constraints.*;
import org.entity.DonationType;

import java.math.BigDecimal;
import java.time.LocalDate;

// Used when clients Post any donations made
public class DonationRequest {

    @NotBlank(message = "Donor name is required")
    @Size(min = 2, max = 255, message = "Donor name must be between 2 and 255 characters")
    private String donorName;

    @NotNull(message = "Donation type is required")
    private DonationType donationType;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Quantity must have at most 10 digits and 2 decimal places")
    private BigDecimal quantity;

    //defaults to today's date if not provided
    private LocalDate donationDate;

    //constructors

    public DonationRequest() {
    }

    public DonationRequest(String donorName, DonationType donationType, BigDecimal quantity, LocalDate donationDate) {
        this.donorName = donorName;
        this.donationType = donationType;
        this.quantity = quantity;
        this.donationDate = donationDate;
    }

    //getters & setters

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

    @Override
    public String toString() {
        return "DonationRequest{" +
                "donorName='" + donorName + '\'' +
                ", donationType=" + donationType +
                ", quantity=" + quantity +
                ", donationDate=" + donationDate +
                '}';
    }
}