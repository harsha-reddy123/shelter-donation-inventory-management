package org.services;

import org.dto.InventoryCheckResponse;
import org.entity.Distribution;
import org.entity.DonationType;
import org.mapper.DistributionMapper;
import org.repository.DistributionRepository;
import org.dto.DistributionResponse;
import org.dto.DistributionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

//service layer for Distribution operations
@Service
@Transactional
public class DistributionService {

    private final DistributionRepository distributionRepository;
    private final ReportService reportService;

    @Autowired
    public DistributionService(DistributionRepository distributionRepository, ReportService reportService) {
        this.distributionRepository = distributionRepository;
        this.reportService = reportService;
    }

    /**
     * Record a new distribution
     * @param request - distribution details
     * return => saved distribution response
     */
    public DistributionResponse recordDistribution(DistributionRequest request) {

        // Convert DTO to Entity
        Distribution distribution = DistributionMapper.toEntity(request);

        // validate before saving the entry
        InventoryCheckResponse validation = reportService.checkInventory(distribution.getDonationType(), distribution.getQuantity());

        if (!validation.isSufficient()) {
            throw new IllegalArgumentException(
                    "Distribution is inappropriate unable to record distribution not matching with the funds"
            );
        }

        // Save to database
        Distribution savedDistribution = distributionRepository.save(distribution);

        // Convert Entity to Response DTO
        return DistributionMapper.toResponse(savedDistribution);
    }

    /**
     * Get all distributions
     * return => list of all distributions
     */
    @Transactional(readOnly = true)
    public List<DistributionResponse> getAllDistributions() {
        return distributionRepository.findAll()
                .stream()
                .map(DistributionMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get distribution by ID
     * @param id - distribution ID
     * return => distribution response
     */
    @Transactional(readOnly = true)
    public DistributionResponse getDistributionById(Long id) {
        Distribution distribution = distributionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distribution not found with id: " + id));
        return DistributionMapper.toResponse(distribution);
    }

    /**
     * Get all distributions by type
     * @param donationType - type of donation
     * return => list of distributions of this type
     */
    @Transactional(readOnly = true)
    public List<DistributionResponse> getDistributionsByType(DonationType donationType) {
        return distributionRepository.findByDonationType(donationType)
                .stream()
                .map(DistributionMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get distributions within a date range
     * @param startDate - start date
     * @param endDate - end date
     * return => list of distributions in date range
     */
    @Transactional(readOnly = true)
    public List<DistributionResponse> getDistributionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return distributionRepository.findByDistributionDateBetween(startDate, endDate)
                .stream()
                .map(DistributionMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get distributions by recipient
     * @param recipient - recipient name
     * return = > list of distributions to this recipient
     */
    @Transactional(readOnly = true)
    public List<DistributionResponse> getDistributionsByRecipient(String recipient) {
        return distributionRepository.findByRecipient(recipient)
                .stream()
                .map(DistributionMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Search distributions by recipient (partial match)
     * @param searchTerm - search term
     * return => list of matching distributions
     */
    @Transactional(readOnly = true)
    public List<DistributionResponse> searchDistributionsByRecipient(String searchTerm) {
        return distributionRepository.findByRecipientContainingIgnoreCase(searchTerm)
                .stream()
                .map(DistributionMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all unique recipients
     * return => list of recipient names
     */
    @Transactional(readOnly = true)
    public List<String> getAllRecipients() {
        return distributionRepository.findAllUniqueRecipients();
    }

    /**
     * Get total quantity distributed for a specific type
     * @param donationType - donation type
     * return => total quantity distributed
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalByType(DonationType donationType) {
        BigDecimal total = distributionRepository.getTotalQuantityByType(donationType);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Get recent distributions (last N days)
     * @param days - number of days to look back
     * return => list of recent distributions
     */
    @Transactional(readOnly = true)
    public List<DistributionResponse> getRecentDistributions(int days) {
        LocalDate sinceDate = LocalDate.now().minusDays(days);
        return distributionRepository.findRecentDistributions(sinceDate)
                .stream()
                .map(DistributionMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Delete a distribution by ID
     * @param id - distribution ID
     */
    public void deleteDistribution(Long id) {
        if (!distributionRepository.existsById(id)) {
            throw new RuntimeException("Distribution not found with id: " + id);
        }
        distributionRepository.deleteById(id);
    }

    /**
     * Count total distributions
     * return total count
     */
    @Transactional(readOnly = true)
    public long getTotalDistributionCount() {
        return distributionRepository.count();
    }
}