package org.dto;

import org.entity.DonationType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//current inventory status grouped by donation type
public class InventoryReportDTO {

    private List<InventoryItem> items;
    private BigDecimal totalValue; // total monetary value if applicable
    private BigDecimal totalQuantity;

    public InventoryReportDTO() {
        this.items = new ArrayList<>();
        this.totalValue = BigDecimal.ZERO;
        this.totalQuantity = BigDecimal.ZERO;
    }

    //Inventory Items

    public static class InventoryItem {
        private DonationType donationType;
        private BigDecimal totalDonated;
        private BigDecimal totalDistributed;
        private BigDecimal currentStock;

        public InventoryItem() {
        }

        public InventoryItem(DonationType donationType, BigDecimal totalDonated,
                             BigDecimal totalDistributed, BigDecimal currentStock) {
            this.donationType = donationType;
            this.totalDonated = totalDonated;
            this.totalDistributed = totalDistributed;
            this.currentStock = currentStock;
        }

        // getters & setters
        public DonationType getDonationType() {
            return donationType;
        }

        public void setDonationType(DonationType donationType) {
            this.donationType = donationType;
        }

        public BigDecimal getTotalDonated() {
            return totalDonated;
        }

        public void setTotalDonated(BigDecimal totalDonated) {
            this.totalDonated = totalDonated;
        }

        public BigDecimal getTotalDistributed() {
            return totalDistributed;
        }

        public void setTotalDistributed(BigDecimal totalDistributed) {
            this.totalDistributed = totalDistributed;
        }

        public BigDecimal getCurrentStock() {
            return currentStock;
        }

        public void setCurrentStock(BigDecimal currentStock) {
            this.currentStock = currentStock;
        }

        @Override
        public String toString() {
            return "InventoryItem{" +
                    "donationType=" + donationType +
                    ", totalDonated=" + totalDonated +
                    ", totalDistributed=" + totalDistributed +
                    ", currentStock=" + currentStock +
                    '}';
        }
    }

    // getters and setters

    public List<InventoryItem> getItems() {
        return items;
    }

    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void addItem(InventoryItem item) {
        this.items.add(item);
    }

    @Override
    public String toString() {
        return "InventoryReportDTO{" +
                "items=" + items +
                ", totalValue=" + totalValue +
                '}';
    }
}
