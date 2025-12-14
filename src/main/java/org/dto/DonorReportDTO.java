package org.dto;

import org.entity.DonationType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// total contributions received from each donor
public class DonorReportDTO {

    private List<DonorContribution> contributions;
    private int totalDonors;

    public DonorReportDTO() {
        this.contributions = new ArrayList<>();
        this.totalDonors = 0;
    }

    //Donor Contributions

    public static class DonorContribution {
        private String donorName;
        private List<DonationByType> donations;
        private BigDecimal totalValue; // for money donations
        private BigDecimal totalQuantity = BigDecimal.ZERO;

        public DonorContribution() {
            this.donations = new ArrayList<>();
            this.totalValue = BigDecimal.ZERO;
        }

        public DonorContribution(String donorName) {
            this.donorName = donorName;
            this.donations = new ArrayList<>();
            this.totalValue = BigDecimal.ZERO;
        }

        // Getters and Setters
        public String getDonorName() {
            return donorName;
        }

        public BigDecimal getTotalQuantity() {
            return totalQuantity;
        }

        public void setTotalQuantity(BigDecimal totalQuantity) {
            this.totalQuantity = totalQuantity;
        }

        public void setDonorName(String donorName) {
            this.donorName = donorName;
        }

        public List<DonationByType> getDonations() {
            return donations;
        }

        public void setDonations(List<DonationByType> donations) {
            this.donations = donations;
        }

        public BigDecimal getTotalValue() {
            return totalValue;
        }

        public void setTotalValue(BigDecimal totalValue) {
            this.totalValue = totalValue;
        }

        public void addDonation(DonationByType donation) {
            this.donations.add(donation);
        }

        @Override
        public String toString() {
            return "DonorContribution{" +
                    "donorName='" + donorName + '\'' +
                    ", donations=" + donations +
                    ", totalValue=" + totalValue +
                    '}';
        }
    }

    //Donation by Type

    public static class DonationByType {
        private DonationType donationType;
        private BigDecimal quantity;

        public DonationByType() {
        }

        public DonationByType(DonationType donationType, BigDecimal quantity) {
            this.donationType = donationType;
            this.quantity = quantity;
        }

        // Getters and Setters
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

        @Override
        public String toString() {
            return "DonationByType{" +
                    "donationType=" + donationType +
                    ", quantity=" + quantity +
                    '}';
        }
    }

    //Getters and Setters

    public List<DonorContribution> getContributions() {
        return contributions;
    }

    public void setContributions(List<DonorContribution> contributions) {
        this.contributions = contributions;
    }

    public int getTotalDonors() {
        return totalDonors;
    }

    public void setTotalDonors(int totalDonors) {
        this.totalDonors = totalDonors;
    }

    public void addContribution(DonorContribution contribution) {
        this.contributions.add(contribution);
        this.totalDonors = this.contributions.size();
    }

    @Override
    public String toString() {
        return "DonorReportDTO{" +
                "contributions=" + contributions +
                ", totalDonors=" + totalDonors +
                '}';
    }
}