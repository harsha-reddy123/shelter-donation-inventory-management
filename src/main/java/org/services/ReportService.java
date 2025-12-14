package org.services;

import org.entity.DonationType;
import org.repository.DistributionRepository;
import org.repository.DonationRepository;
import org.dto.InventoryReportDTO;
import org.dto.DonorReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

//Service for generating reports
@Service
@Transactional(readOnly = true)
public class ReportService {

    private final DonationRepository donationRepository;
    private final DistributionRepository distributionRepository;

    @Autowired
    public ReportService(DonationRepository donationRepository,
                         DistributionRepository distributionRepository) {
        this.donationRepository = donationRepository;
        this.distributionRepository = distributionRepository;
    }

    /**
     * Generate Inventory Report
     * Shows current stock by donation type (Donated - Distributed)
     * returns => InventoryReportDTO containing current inventory status
     */
    public InventoryReportDTO generateInventoryReport() {
        InventoryReportDTO report = new InventoryReportDTO();

        // Get total donated by type
        Map<DonationType, BigDecimal> donatedMap = getTotalDonatedByType();

        // Get total distributed by type
        Map<DonationType, BigDecimal> distributedMap = getTotalDistributedByType();

        // Get all donation types that have been used
        Set<DonationType> allTypes = new HashSet<>();
        allTypes.addAll(donatedMap.keySet());
        allTypes.addAll(distributedMap.keySet());

        // Calculate current stock for each type
        BigDecimal totalMoneyInStock = BigDecimal.ZERO;

        for (DonationType type : allTypes) {
            BigDecimal donated = donatedMap.getOrDefault(type, BigDecimal.ZERO);
            BigDecimal distributed = distributedMap.getOrDefault(type, BigDecimal.ZERO);
            BigDecimal currentStock = donated.subtract(distributed);

            // Create inventory item
            InventoryReportDTO.InventoryItem item = new InventoryReportDTO.InventoryItem(
                    type,
                    donated,
                    distributed,
                    currentStock
            );

            report.addItem(item);

            // If this is money, add to total value
            if (type == DonationType.MONEY) {
                totalMoneyInStock = totalMoneyInStock.add(currentStock);
            }
        }

        report.setTotalValue(totalMoneyInStock);

        return report;
    }

    /**
     * Generate Donor Report
     * Shows total contributions by each donor, grouped by type
     * returns => DonorReportDTO containing donor contributions
     */
    public DonorReportDTO generateDonorReport() {
        DonorReportDTO report = new DonorReportDTO();

        // Get donations grouped by donor and type
        List<Object[]> donorData = donationRepository.getTotalDonationsByDonor();

        // Group by donor name
        Map<String, DonorReportDTO.DonorContribution> donorMap = new LinkedHashMap<>();

        for (Object[] row : donorData) {
            String donorName = (String) row[0];
            DonationType donationType = (DonationType) row[1];
            BigDecimal quantity = (BigDecimal) row[2];

            // Get or create donor contribution
            DonorReportDTO.DonorContribution contribution = donorMap.computeIfAbsent(
                    donorName,
                    k -> new DonorReportDTO.DonorContribution(donorName)
            );

            // Add donation by type
            DonorReportDTO.DonationByType donationByType =
                    new DonorReportDTO.DonationByType(donationType, quantity);
            contribution.addDonation(donationByType);

            // If money, add to total value
            if (donationType == DonationType.MONEY) {
                contribution.setTotalValue(
                        contribution.getTotalValue().add(quantity)
                );
            }
        }

        // Add all contributions to report
        for (DonorReportDTO.DonorContribution contribution : donorMap.values()) {
            report.addContribution(contribution);
        }

        return report;
    }

    /**
     * Get inventory for a specific donation type
     * @param donationType - the type to check
     * returns => current stock quantity
     */
    public BigDecimal getInventoryByType(DonationType donationType) {
        BigDecimal donated = getTotalDonatedForType(donationType);
        BigDecimal distributed = getTotalDistributedForType(donationType);
        return donated.subtract(distributed);
    }

    /**
     * Check if sufficient inventory exists for distribution
     * @param donationType - type to check
     * @param requestedQuantity - quantity requested
     * returns => true if enough inventory available
     */
    public boolean hasAvailableInventory(DonationType donationType, BigDecimal requestedQuantity) {
        BigDecimal available = getInventoryByType(donationType);
        return available.compareTo(requestedQuantity) >= 0;
    }

    //helper methods to handle internal functions

    // Get total donated by type as a map
    private Map<DonationType, BigDecimal> getTotalDonatedByType() {
        return getDonationTypeBigDecimalMap(donationRepository.getTotalQuantityByType());
    }

    private Map<DonationType, BigDecimal> getDonationTypeBigDecimalMap(List<Object[]> totalQuantityByType) {
        Map<DonationType, BigDecimal> map = new HashMap<>();

        for (Object[] row : totalQuantityByType) {
            DonationType type = (DonationType) row[0];
            BigDecimal quantity = (BigDecimal) row[1];
            map.put(type, quantity);
        }

        return map;
    }

    //Get total distributed by type as a map
    private Map<DonationType, BigDecimal> getTotalDistributedByType() {
        return getDonationTypeBigDecimalMap(distributionRepository.getTotalQuantityByType());
    }

    //Get total donated for a specific type
    private BigDecimal getTotalDonatedForType(DonationType donationType) {
        Map<DonationType, BigDecimal> map = getTotalDonatedByType();
        return map.getOrDefault(donationType, BigDecimal.ZERO);
    }

    //Get total distributed for a specific type
    private BigDecimal getTotalDistributedForType(DonationType donationType) {
        BigDecimal total = distributionRepository.getTotalQuantityByType(donationType);
        return total != null ? total : BigDecimal.ZERO;
    }
}
