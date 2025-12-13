package org.repository;

import org.entity.Donation;
import org.entity.DonationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByDonorName(String donorName);

    List<Donation> findByDonationType(DonationType donationType);

    List<Donation> findByDonorNameContainingIgnoreCase(String donorName);

    List<Donation> findByDonationDateBetween(LocalDate startDate, LocalDate endDate);

    List<Donation> findByDonationTypeAndDonationDateBetween(
            DonationType donationType,
            LocalDate startDate,
            LocalDate endDate
    );

    //custom queries
    /**
     * Calculate total quantity donated by type
     * Return => [DonationType, TotalQuantity] pairs
     */
    @Query("SELECT d.donationType, SUM(d.quantity) " +
            "FROM Donation d " +
            "GROUP BY d.donationType")
    List<Object[]> getTotalQuantityByType();

    /**
     * Calculate total donations by each donor
     * Return => [DonorName, DonationType, TotalQuantity] tuples
     */
    @Query("SELECT d.donorName, d.donationType, SUM(d.quantity) " +
            "FROM Donation d " +
            "GROUP BY d.donorName, d.donationType " +
            "ORDER BY d.donorName")
    List<Object[]> getTotalDonationsByDonor();

    //Get total quantity donated by a specific donor
    @Query("SELECT SUM(d.quantity) FROM Donation d WHERE d.donorName = :donorName")
    BigDecimal getTotalQuantityByDonor(@Param("donorName") String donorName);

    //get all unique donor names
    @Query("SELECT DISTINCT d.donorName FROM Donation d ORDER BY d.donorName")
    List<String> findAllUniqueDonorNames();

    //Count total donations by type
    long countByDonationType(DonationType donationType);

    // Find recent donations (last x days)
    @Query("SELECT d FROM Donation d WHERE d.donationDate >= :sinceDate ORDER BY d.donationDate DESC")
    List<Donation> findRecentDonations(@Param("sinceDate") LocalDate sinceDate);
}
