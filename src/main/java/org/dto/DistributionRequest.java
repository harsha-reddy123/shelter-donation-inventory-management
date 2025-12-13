package org.dto;

import jakarta.validation.constraints.*;
import org.entity.DonationType;

import java.math.BigDecimal;
import java.time.LocalDate;

//Used when clients want to know where is it distributed
public class DistributionRequest {

    @NotNull(message = "Donation type is required")
    private DonationType donationType;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Quantity must have at most 10 digits and 2 decimal places")
    private BigDecimal quantity;

    // defaults to today's date if not provided
    private LocalDate distributionDate;

    // who received the distribution
    @Size(max = 255, message = "Recipient name must not exceed 255 characters")
    private String recipient;

    //constructors

    public DistributionRequest() {
    }

    public DistributionRequest(DonationType donationType, BigDecimal quantity,
                               LocalDate distributionDate, String recipient) {
        this.donationType = donationType;
        this.quantity = quantity;
        this.distributionDate = distributionDate;
        this.recipient = recipient;
    }

    // getters & setters

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

    @Override
    public String toString() {
        return "DistributionRequest{" +
                "donationType=" + donationType +
                ", quantity=" + quantity +
                ", distributionDate=" + distributionDate +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}
