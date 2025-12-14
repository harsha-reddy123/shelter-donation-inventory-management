package org.services;


import org.dto.DonorReportDTO;
import org.dto.InventoryCheckResponse;
import org.dto.InventoryReportDTO;
import org.entity.DonationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.repository.DistributionRepository;
import org.repository.DonationRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 inventory calculation: Current Stock = Donated - Distributed
 donor report: Total contributions by each donor
 */
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private DonationRepository donationRepository;

    @Mock
    private DistributionRepository distributionRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        // Reset mocks before each test
        reset(donationRepository, distributionRepository);
    }

    @Test
    @DisplayName("Should generate inventory report with no distributions")
    void testInventoryReportWithoutDistributions() {
        // Harsha donated: MONEY=2516.08, CLOTHING=450, FOOD=100
        // Test 1 donated: TOYS=55
        Object[] moneyData = {DonationType.MONEY, new BigDecimal("2516.08")};
        Object[] clothingData = {DonationType.CLOTHING, new BigDecimal("450.00")};
        Object[] foodData = {DonationType.FOOD, new BigDecimal("100.00")};
        Object[] toysData = {DonationType.TOYS, new BigDecimal("55.00")};

        List<Object[]> donatedByType = Arrays.asList(moneyData, clothingData, foodData, toysData);
        when(donationRepository.getTotalQuantityByType()).thenReturn(donatedByType);


        when(distributionRepository.getTotalQuantityByType()).thenReturn(Arrays.asList());

        // When
        InventoryReportDTO report = reportService.generateInventoryReport();

        // Then
        assertNotNull(report);
        assertNotNull(report.getItems());
        assertEquals(4, report.getItems().size());

        // Verify MONEY item
        InventoryReportDTO.InventoryItem moneyItem = findItemByType(report, DonationType.MONEY);
        assertNotNull(moneyItem);
        assertEquals(DonationType.MONEY, moneyItem.getDonationType());
        assertEquals(0, new BigDecimal("2516.08").compareTo(moneyItem.getTotalDonated()));
        assertEquals(0, BigDecimal.ZERO.compareTo(moneyItem.getTotalDistributed()));
        assertEquals(0, new BigDecimal("2516.08").compareTo(moneyItem.getCurrentStock()));

        // Verify CLOTHING item
        InventoryReportDTO.InventoryItem clothingItem = findItemByType(report, DonationType.CLOTHING);
        assertNotNull(clothingItem);
        assertEquals(0, new BigDecimal("450.00").compareTo(clothingItem.getTotalDonated()));
        assertEquals(0, BigDecimal.ZERO.compareTo(clothingItem.getTotalDistributed()));
        assertEquals(0, new BigDecimal("450.00").compareTo(clothingItem.getCurrentStock()));

        // Verify FOOD item
        InventoryReportDTO.InventoryItem foodItem = findItemByType(report, DonationType.FOOD);
        assertNotNull(foodItem);
        assertEquals(0, new BigDecimal("100.00").compareTo(foodItem.getTotalDonated()));
        assertEquals(0, new BigDecimal("100.00").compareTo(foodItem.getCurrentStock()));

        // Verify TOYS item
        InventoryReportDTO.InventoryItem toysItem = findItemByType(report, DonationType.TOYS);
        assertNotNull(toysItem);
        assertEquals(0, new BigDecimal("55.00").compareTo(toysItem.getTotalDonated()));
        assertEquals(0, new BigDecimal("55.00").compareTo(toysItem.getCurrentStock()));

        // Verify total value (only MONEY counts)
        assertEquals(0, new BigDecimal("2516.08").compareTo(report.getTotalValue()));

        verify(donationRepository, times(1)).getTotalQuantityByType();
        verify(distributionRepository, times(1)).getTotalQuantityByType();
    }

    @Test
    @DisplayName("Should calculate inventory correctly with distributions (Donated - Distributed)")
    void testInventoryReportWithDistributions() {
        // Donations: FOOD=500, CLOTHING=300
        Object[] foodDonated = {DonationType.FOOD, new BigDecimal("500.00")};
        Object[] clothingDonated = {DonationType.CLOTHING, new BigDecimal("300.00")};
        when(donationRepository.getTotalQuantityByType()).thenReturn(Arrays.asList(foodDonated, clothingDonated));

        // Distributions: FOOD=200, CLOTHING=100
        Object[] foodDistributed = {DonationType.FOOD, new BigDecimal("200.00")};
        Object[] clothingDistributed = {DonationType.CLOTHING, new BigDecimal("100.00")};
        when(distributionRepository.getTotalQuantityByType()).thenReturn(Arrays.asList(foodDistributed, clothingDistributed));

        // When
        InventoryReportDTO report = reportService.generateInventoryReport();

        // Then
        assertNotNull(report);
        assertEquals(2, report.getItems().size());

        // Verify FOOD: Current Stock = 500 - 200 = 300
        InventoryReportDTO.InventoryItem foodItem = findItemByType(report, DonationType.FOOD);
        assertNotNull(foodItem);
        assertEquals(0, new BigDecimal("500.00").compareTo(foodItem.getTotalDonated()));
        assertEquals(0, new BigDecimal("200.00").compareTo(foodItem.getTotalDistributed()));
        assertEquals(0, new BigDecimal("300.00").compareTo(foodItem.getCurrentStock()));

        // Verify CLOTHING: Current Stock = 300 - 100 = 200
        InventoryReportDTO.InventoryItem clothingItem = findItemByType(report, DonationType.CLOTHING);
        assertNotNull(clothingItem);
        assertEquals(0, new BigDecimal("300.00").compareTo(clothingItem.getTotalDonated()));
        assertEquals(0, new BigDecimal("100.00").compareTo(clothingItem.getTotalDistributed()));
        assertEquals(0, new BigDecimal("200.00").compareTo(clothingItem.getCurrentStock()));
    }

    @Test
    @DisplayName("Should handle zero stock (all distributed)")
    void testInventoryReportWithZeroStock() {
        // Donated 100, Distributed 100 = 0 stock
        List<Object[]> donatedList = new ArrayList<>();
        donatedList.add(new Object[] { DonationType.FOOD, new BigDecimal("100.00") });

        when(donationRepository.getTotalQuantityByType()).thenReturn(donatedList);


        //Object[] foodDistributed = {DonationType.FOOD, new BigDecimal("100.00")};

        List<Object[]> foodDistributed = new ArrayList<>();
        foodDistributed.add(new Object[] { DonationType.FOOD, new BigDecimal("100.00") });

        when(distributionRepository.getTotalQuantityByType()).thenReturn(foodDistributed);

        // When
        InventoryReportDTO report = reportService.generateInventoryReport();

        // Then
        assertNotNull(report);
        assertEquals(1, report.getItems().size());

        InventoryReportDTO.InventoryItem foodItem = findItemByType(report, DonationType.FOOD);
        assertNotNull(foodItem);
        assertEquals(0, BigDecimal.ZERO.compareTo(foodItem.getCurrentStock()));
    }

    @Test
    @DisplayName("Should generate donor report with multiple donors and types")
    void testDonorReportMultipleDonorsAndTypes() {
        // Harsha: CLOTHING=450, FOOD=100, MONEY=2516.08
        // Test 1: TOYS=55
        Object[] harshaClothing = {"Harsha", DonationType.CLOTHING, new BigDecimal("450.00")};
        Object[] harshaFood = {"Harsha", DonationType.FOOD, new BigDecimal("100.00")};
        Object[] harshaMoney = {"Harsha", DonationType.MONEY, new BigDecimal("2516.08")};
        Object[] test1Toys = {"Test 1", DonationType.TOYS, new BigDecimal("55.00")};

        List<Object[]> donorData = Arrays.asList(harshaMoney, harshaClothing, harshaFood, test1Toys);
        when(donationRepository.getTotalDonationsByDonor()).thenReturn(donorData);

        // When
        DonorReportDTO report = reportService.generateDonorReport();

        // Then
        assertNotNull(report);
        assertEquals(2, report.getTotalDonors());
        assertEquals(2, report.getContributions().size());

        // Verify Harsha's contribution
        DonorReportDTO.DonorContribution harshaContribution = findContributionByDonor(report, "Harsha");
        assertNotNull(harshaContribution);
        assertEquals("Harsha", harshaContribution.getDonorName());
        assertEquals(3, harshaContribution.getDonations().size());
        assertEquals(0, new BigDecimal("2516.08").compareTo(harshaContribution.getTotalValue()));

        // Verify Harsha's MONEY donation
        DonorReportDTO.DonationByType harshaMoneyDonation = findDonationByType(harshaContribution, DonationType.MONEY);
        assertNotNull(harshaMoneyDonation);
        assertEquals(0, new BigDecimal("2516.08").compareTo(harshaMoneyDonation.getQuantity()));

        // Verify Harsha's CLOTHING donation
        DonorReportDTO.DonationByType harshaClothingDonation = findDonationByType(harshaContribution, DonationType.CLOTHING);
        assertNotNull(harshaClothingDonation);
        assertEquals(0, new BigDecimal("450.00").compareTo(harshaClothingDonation.getQuantity()));

        // Verify Harsha's FOOD donation
        DonorReportDTO.DonationByType harshaFoodDonation = findDonationByType(harshaContribution, DonationType.FOOD);
        assertNotNull(harshaFoodDonation);
        assertEquals(0, new BigDecimal("100.00").compareTo(harshaFoodDonation.getQuantity()));

        // Verify Test 1's contribution
        DonorReportDTO.DonorContribution test1Contribution = findContributionByDonor(report, "Test 1");
        assertNotNull(test1Contribution);
        assertEquals("Test 1", test1Contribution.getDonorName());
        assertEquals(1, test1Contribution.getDonations().size());
        assertEquals(0, BigDecimal.ZERO.compareTo(test1Contribution.getTotalValue())); // TOYS, not MONEY

        // Verify Test 1's TOYS donation
        DonorReportDTO.DonationByType test1ToysDonation = findDonationByType(test1Contribution, DonationType.TOYS);
        assertNotNull(test1ToysDonation);
        assertEquals(0, new BigDecimal("55.00").compareTo(test1ToysDonation.getQuantity()));

        verify(donationRepository, times(1)).getTotalDonationsByDonor();
    }

    @Test
    @DisplayName("Should return empty report when no donations exist")
    void testEmptyInventoryReport() {
        // Given
        when(donationRepository.getTotalQuantityByType()).thenReturn(Arrays.asList());
        when(distributionRepository.getTotalQuantityByType()).thenReturn(Arrays.asList());

        // When
        InventoryReportDTO report = reportService.generateInventoryReport();

        // Then
        assertNotNull(report);
        assertNotNull(report.getItems());
        assertTrue(report.getItems().isEmpty());
        assertEquals(0, BigDecimal.ZERO.compareTo(report.getTotalValue()));
    }

    @Test
    @DisplayName("Should return empty donor report when no donations exist")
    void testEmptyDonorReport() {
        // Given
        when(donationRepository.getTotalDonationsByDonor()).thenReturn(Arrays.asList());

        // When
        DonorReportDTO report = reportService.generateDonorReport();

        // Then
        assertNotNull(report);
        assertEquals(0, report.getTotalDonors());
        assertTrue(report.getContributions().isEmpty());
    }

    @Test
    @DisplayName("Should get inventory for specific type")
    void testGetInventoryByType() {
        // Given
        List<Object[]> foodDonated = new ArrayList<>();
        foodDonated.add(new Object[] { DonationType.FOOD, new BigDecimal("500.00") });

        when(donationRepository.getTotalQuantityByType()).thenReturn(foodDonated);

        List<Object[]> foodDistributed = new ArrayList<>();
        foodDistributed.add(new Object[] { DonationType.FOOD, new BigDecimal("200.00") });

        when(distributionRepository.getTotalQuantityByType()).thenReturn(foodDistributed);

        when(distributionRepository.getTotalQuantityByType(DonationType.FOOD))
                .thenReturn(new BigDecimal("200.00"));

        // When
        BigDecimal inventory = reportService.getInventoryByType(DonationType.FOOD);

        // Then
        assertNotNull(inventory);
        assertEquals(0, new BigDecimal("300.00").compareTo(inventory)); // 500 - 200 = 300
    }

    @Test
    @DisplayName("Should check if inventory is available for distribution")
    void testHasAvailableInventory() {
        // Given
        List<Object[]> foodDonated = new ArrayList<>();
        foodDonated.add(new Object[] { DonationType.FOOD, new BigDecimal("500.00") });

        when(donationRepository.getTotalQuantityByType()).thenReturn(foodDonated);

        List<Object[]> foodDistributed = new ArrayList<>();
        foodDistributed.add(new Object[] { DonationType.FOOD, new BigDecimal("200.00") });

        when(distributionRepository.getTotalQuantityByType()).thenReturn(foodDistributed);

        // When - Check if 250 is available (should be true: 500-200=300 available)
        InventoryCheckResponse validation = reportService.checkInventory(DonationType.FOOD, new BigDecimal("250.00"));
        System.out.println(validation.isSufficient());

        // Then
        assertTrue(validation.isSufficient());
    }

    @Test
    @DisplayName("Should return false when insufficient inventory for distribution")
    void testInsufficientInventory() {
        // Given
        List<Object[]> foodDonated = new ArrayList<>();
        foodDonated.add(new Object[] { DonationType.FOOD, new BigDecimal("100.00") });

        when(donationRepository.getTotalQuantityByType()).thenReturn(foodDonated);

        List<Object[]> foodDistributed = new ArrayList<>();
        foodDistributed.add(new Object[] { DonationType.FOOD, new BigDecimal("80.00") });

        when(distributionRepository.getTotalQuantityByType()).thenReturn(foodDistributed);

        when(distributionRepository.getTotalQuantityByType(DonationType.FOOD))
                .thenReturn(new BigDecimal("80.00"));

        // When - Try to distribute 50 (only 20 available: 100-80=20)
        InventoryCheckResponse validation = reportService.checkInventory(DonationType.FOOD, new BigDecimal("50.00"));

        // Then
        assertFalse(validation.isSufficient());
    }

    @Test
    @DisplayName("Should handle single donor with single donation type")
    void testDonorReportSingleDonorSingleType() {

        // Given
        List<Object[]> singleDonation = new ArrayList<>();
        singleDonation.add(new Object[] {
                "Harsha",
                DonationType.MONEY,
                new BigDecimal("1000.00")
        });

        when(donationRepository.getTotalDonationsByDonor())
                .thenReturn(singleDonation);

        // When
        DonorReportDTO report = reportService.generateDonorReport();

        // Then
        assertNotNull(report);
        assertEquals(1, report.getTotalDonors());
        assertEquals(1, report.getContributions().size());

        DonorReportDTO.DonorContribution contribution = report.getContributions().get(0);
        assertEquals("Harsha", contribution.getDonorName());
        assertEquals(1, contribution.getDonations().size());
        assertEquals(0, new BigDecimal("1000.00")
                .compareTo(contribution.getTotalValue()));
    }


    @Test
    @DisplayName("Should calculate totalValue correctly (only MONEY type)")
    void testTotalValueCalculation() {
        // Mix of MONEY and non-MONEY donations
        Object[] foodDonated = {DonationType.FOOD, new BigDecimal("500.00")};
        Object[] moneyDonated = {DonationType.MONEY, new BigDecimal("10000.00")};
        Object[] clothingDonated = {DonationType.CLOTHING, new BigDecimal("300.00")};

        when(donationRepository.getTotalQuantityByType())
                .thenReturn(Arrays.asList(foodDonated, moneyDonated, clothingDonated));

        // Distribute some MONEY
        List<Object[]> moneyDistributed = new ArrayList<>();
        moneyDistributed.add(new Object[] { DonationType.MONEY, new BigDecimal("3000.00") });

        when(distributionRepository.getTotalQuantityByType()).thenReturn(moneyDistributed);

        // When
        InventoryReportDTO report = reportService.generateInventoryReport();

        // Then - totalValue should be current MONEY stock: 10000 - 3000 = 7000
        assertEquals(0, new BigDecimal("7000.00").compareTo(report.getTotalValue()));
    }

    @Test
    @DisplayName("Should handle negative stock scenario (over-distributed)")
    void testNegativeStock() {
        // Distributed more than donated (edge case - should be prevented by validation)
        List<Object[]> foodDonated = new ArrayList<>();
        foodDonated.add(new Object[] { DonationType.FOOD, new BigDecimal("100.00") });

        when(donationRepository.getTotalQuantityByType()).thenReturn(foodDonated);

        List<Object[]> foodDistributed = new ArrayList<>();
        foodDistributed.add(new Object[] { DonationType.FOOD, new BigDecimal("150.00") });

        when(distributionRepository.getTotalQuantityByType()).thenReturn(foodDistributed);

        // When
        InventoryReportDTO report = reportService.generateInventoryReport();

        // Then - Should show negative stock (currentStock = 100 - 150 = -50)
        InventoryReportDTO.InventoryItem foodItem = findItemByType(report, DonationType.FOOD);
        assertNotNull(foodItem);
        assertTrue(foodItem.getCurrentStock().compareTo(BigDecimal.ZERO) < 0);
        assertEquals(0, new BigDecimal("-50.00").compareTo(foodItem.getCurrentStock()));
    }

    // Helper Methods

    private InventoryReportDTO.InventoryItem findItemByType(InventoryReportDTO report, DonationType type) {
        return report.getItems().stream()
                .filter(item -> item.getDonationType() == type)
                .findFirst()
                .orElse(null);
    }

    private DonorReportDTO.DonorContribution findContributionByDonor(DonorReportDTO report, String donorName) {
        return report.getContributions().stream()
                .filter(c -> c.getDonorName().equals(donorName))
                .findFirst()
                .orElse(null);
    }

    private DonorReportDTO.DonationByType findDonationByType(DonorReportDTO.DonorContribution contribution, DonationType type) {
        return contribution.getDonations().stream()
                .filter(d -> d.getDonationType() == type)
                .findFirst()
                .orElse(null);
    }
}
