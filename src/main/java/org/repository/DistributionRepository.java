package org.repository;

import org.entity.Distribution;
import org.entity.DonationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Long> {

    List<Distribution> findByDonationType(DonationType donationType);

    List<Distribution> findByDistributionDateBetween(LocalDate startDate, LocalDate endDate);


    List<Distribution> findByDonationTypeAndDistributionDateBetween(
            DonationType donationType,
            LocalDate startDate,
            LocalDate endDate
    );


    List<Distribution> findByRecipient(String recipient);


    List<Distribution> findByRecipientContainingIgnoreCase(String recipient);

    // Custom Queries

    /**
     * Calculate total quantity distributed by type
     * Used for inventory report calculation (Donations - Distributions)
     * Returns = > [DonationType, TotalQuantity] pairs
     */
    @Query("SELECT d.donationType, SUM(d.quantity) " +
            "FROM Distribution d " +
            "GROUP BY d.donationType")
    List<Object[]> getTotalQuantityByType();

    // Get total quantity distributed for a specific type
    @Query("SELECT SUM(d.quantity) FROM Distribution d WHERE d.donationType = :donationType")
    BigDecimal getTotalQuantityByType(@Param("donationType") DonationType donationType);

    // Get distributions grouped by recipient
    @Query("SELECT d.recipient, d.donationType, SUM(d.quantity) " +
            "FROM Distribution d " +
            "WHERE d.recipient IS NOT NULL " +
            "GROUP BY d.recipient, d.donationType " +
            "ORDER BY d.recipient")
    List<Object[]> getDistributionsByRecipient();

    // Count total distributions by type
    long countByDonationType(DonationType donationType);

    // Find recent distributions (last x days)
    @Query("SELECT d FROM Distribution d WHERE d.distributionDate >= :sinceDate ORDER BY d.distributionDate DESC")
    List<Distribution> findRecentDistributions(@Param("sinceDate") LocalDate sinceDate);

    // Get all unique recipients
    @Query("SELECT DISTINCT d.recipient FROM Distribution d WHERE d.recipient IS NOT NULL ORDER BY d.recipient")
    List<String> findAllUniqueRecipients();
}