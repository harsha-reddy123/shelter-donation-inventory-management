package org.services;


import org.dto.DonationRequest;
import org.entity.Donation;
import org.entity.DonationType;
import org.repository.DonationRepository;
import org.dto.DonationResponse;
import org.mapper.DonorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

//service layer for Donation operations
@Service
@Transactional
public class DonationService {

    private final DonationRepository donationRepository;

    @Autowired
    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    /**
     * Register a new donation
     * @param request - donation details
     * return => saved donation response
     */
    public DonationResponse registerDonation(DonationRequest request) {
        // Convert DTO to Entity
        Donation donation = DonorMapper.toEntity(request);

        // Save to database
        Donation savedDonation = donationRepository.save(donation);

        // Convert Entity to Response DTO
        return DonorMapper.toResponse(savedDonation);
    }

    /**
     * Get all donations
     * return => list of all donations
     */
    @Transactional(readOnly = true)
    public List<DonationResponse> getAllDonations() {
        System.out.println("getAllDonations");
        return donationRepository.findAll()
                .stream()
                .map(DonorMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get donation by ID
     * @param id - donation ID
     * return => donation response
     */
    @Transactional(readOnly = true)
    public DonationResponse getDonationById(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found with id: " + id));
        return DonorMapper.toResponse(donation);
    }

    /**
     * Get all donations by a specific donor
     * @param donorName - name of the donor
     * return => list of donations from this donor
     */
    @Transactional(readOnly = true)
    public List<DonationResponse> getDonationsByDonor(String donorName) {
        return donationRepository.findByDonorName(donorName)
                .stream()
                .map(DonorMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all donations by type
     * @param donationType - type of donation
     * return => list of donations of this type
     */
    @Transactional(readOnly = true)
    public List<DonationResponse> getDonationsByType(DonationType donationType) {
        return donationRepository.findByDonationType(donationType)
                .stream()
                .map(DonorMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get donations within a date range
     * @param startDate - start date
     * @param endDate - end date
     * return => list of donations in date range
     */
    @Transactional(readOnly = true)
    public List<DonationResponse> getDonationsByDateRange(LocalDate startDate, LocalDate endDate) {
        return donationRepository.findByDonationDateBetween(startDate, endDate)
                .stream()
                .map(DonorMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Search donations by donor name (partial match)
     * @param searchTerm - search term
     * return => list of matching donations
     */
    @Transactional(readOnly = true)
    public List<DonationResponse> searchDonationsByDonor(String searchTerm) {
        return donationRepository.findByDonorNameContainingIgnoreCase(searchTerm)
                .stream()
                .map(DonorMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all unique donor names
     * return => list of donor names
     */
    @Transactional(readOnly = true)
    public List<String> getAllDonorNames() {
        return donationRepository.findAllUniqueDonorNames();
    }

    /**
     * Get total quantity donated by a specific donor
     * @param donorName - donor name
     * return => total quantity
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalByDonor(String donorName) {
        BigDecimal total = donationRepository.getTotalQuantityByDonor(donorName);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Get recent donations (last N days)
     * @param days - number of days to look back
     * return => list of recent donations
     */
    @Transactional(readOnly = true)
    public List<DonationResponse> getRecentDonations(int days) {
        LocalDate sinceDate = LocalDate.now().minusDays(days);
        return donationRepository.findRecentDonations(sinceDate)
                .stream()
                .map(DonorMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Delete a donation by ID
     * @param id - donation ID
     */
    public void deleteDonation(Long id) {
        if (!donationRepository.existsById(id)) {
            throw new RuntimeException("Donation not found with id: " + id);
        }
        donationRepository.deleteById(id);
    }

    /**
     * Count total donations
     * return = > total count
     */
    @Transactional(readOnly = true)
    public long getTotalDonationCount() {
        return donationRepository.count();
    }
}
