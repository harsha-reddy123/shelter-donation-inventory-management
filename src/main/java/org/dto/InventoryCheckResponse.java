package org.dto;

import org.entity.DonationType;

import java.math.BigDecimal;

public class InventoryCheckResponse {
    private DonationType donationType;
    private BigDecimal requestedQuantity;
    private BigDecimal availableQuantity;
    private boolean sufficient;

    public DonationType getDonationType() {
        return donationType;
    }

    public void setDonationType(DonationType donationType) {
        this.donationType = donationType;
    }

    public BigDecimal getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(BigDecimal requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

    public BigDecimal getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(BigDecimal availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public boolean isSufficient() {
        return sufficient;
    }

    public void setSufficient(boolean sufficient) {
        this.sufficient = sufficient;
    }

    @Override
    public String toString() {
        return "InventoryCheckResponse{" +
                "donationType=" + donationType +
                ", requestedQuantity=" + requestedQuantity +
                ", availableQuantity=" + availableQuantity +
                ", sufficient=" + sufficient +
                '}';
    }
}
