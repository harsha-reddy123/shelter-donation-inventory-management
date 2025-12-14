package org.services;


import org.dto.DonationRequest;
import org.dto.DonationResponse;
import org.entity.Donation;
import org.entity.DonationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.repository.DonationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DonationServiceTest {

    @Mock
    private DonationRepository donationRepository;

    @InjectMocks
    private DonationService donationService;

    private Donation testDonation;
    private DonationRequest testRequest;

    @BeforeEach
    void setUp() {
        // Create test donation entity
        testDonation = new Donation(
                "Harsha",
                DonationType.FOOD,
                new BigDecimal("500.00"),
                LocalDate.of(2025, 12, 13)
        );
        testDonation.setId(1L);
        testDonation.setCreatedAt(LocalDateTime.now());

        // Create test donation request
        testRequest = new DonationRequest(
                "Harsha",
                DonationType.FOOD,
                new BigDecimal("100.00"),
                LocalDate.of(2025, 12, 13)
        );
    }

    @Test
    @DisplayName("Should register donation successfully")
    void testRegisterDonation() {
        // Given
        when(donationRepository.save(any(Donation.class))).thenReturn(testDonation);

        // When
        DonationResponse response = donationService.registerDonation(testRequest);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Harsha", response.getDonorName());
        assertEquals(DonationType.FOOD, response.getDonationType());
        assertEquals(0, new BigDecimal("100.00").compareTo(response.getQuantity()));

        verify(donationRepository, times(1)).save(any(Donation.class));
    }

    @Test
    @DisplayName("Should get all donations")
    void testGetAllDonations() {
        // Given
        Donation donation2 = new Donation(
                "Test 1",
                DonationType.TOYS,
                new BigDecimal("55.00"),
                LocalDate.now()
        );
        donation2.setId(2L);
        donation2.setCreatedAt(LocalDateTime.now());

        List<Donation> donations = Arrays.asList(testDonation, donation2);
        when(donationRepository.findAll()).thenReturn(donations);

        // When
        List<DonationResponse> responses = donationService.getAllDonations();

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Harsha", responses.get(0).getDonorName());
        assertEquals("Test 1", responses.get(1).getDonorName());

        verify(donationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get donation by ID")
    void testGetDonationById() {
        // Given
        when(donationRepository.findById(1L)).thenReturn(Optional.of(testDonation));

        // When
        DonationResponse response = donationService.getDonationById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Harsha", response.getDonorName());
        assertEquals(DonationType.FOOD, response.getDonationType());

        verify(donationRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when donation not found by ID")
    void testGetDonationByIdNotFound() {
        // Given
        when(donationRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            donationService.getDonationById(999L);
        });

        assertTrue(exception.getMessage().contains("Donation not found"));
        verify(donationRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get donations by donor name")
    void testGetDonationsByDonor() {
        // Given
        Donation donation2 = new Donation(
                "Harsha",
                DonationType.MONEY,
                new BigDecimal("2516.08"),
                LocalDate.now()
        );
        donation2.setId(2L);
        donation2.setCreatedAt(LocalDateTime.now());

        List<Donation> harshaDonations = Arrays.asList(testDonation, donation2);
        when(donationRepository.findByDonorName("Harsha")).thenReturn(harshaDonations);

        // When
        List<DonationResponse> responses = donationService.getDonationsByDonor("Harsha");

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());

        // Verify all donations are from Harsha
        assertTrue(responses.stream().allMatch(d -> d.getDonorName().equals("Harsha")));

        verify(donationRepository, times(1)).findByDonorName("Harsha");
    }

    @Test
    @DisplayName("Should get donations by type")
    void testGetDonationsByType() {
        // Given
        List<Donation> foodDonations = Arrays.asList(testDonation);
        when(donationRepository.findByDonationType(DonationType.FOOD)).thenReturn(foodDonations);

        // When
        List<DonationResponse> responses = donationService.getDonationsByType(DonationType.FOOD);

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(DonationType.FOOD, responses.get(0).getDonationType());

        verify(donationRepository, times(1)).findByDonationType(DonationType.FOOD);
    }

    @Test
    @DisplayName("Should search donations by donor name")
    void testSearchDonationsByDonor() {
        // Given
        List<Donation> searchResults = Arrays.asList(testDonation);
        when(donationRepository.findByDonorNameContainingIgnoreCase("har")).thenReturn(searchResults);

        // When
        List<DonationResponse> responses = donationService.searchDonationsByDonor("har");

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertTrue(responses.get(0).getDonorName().toLowerCase().contains("har"));

        verify(donationRepository, times(1)).findByDonorNameContainingIgnoreCase("har");
    }

    @Test
    @DisplayName("Should get donations by date range")
    void testGetDonationsByDateRange() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 12, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        List<Donation> donationsInRange = Arrays.asList(testDonation);

        when(donationRepository.findByDonationDateBetween(startDate, endDate))
                .thenReturn(donationsInRange);

        // When
        List<DonationResponse> responses = donationService.getDonationsByDateRange(startDate, endDate);

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());

        verify(donationRepository, times(1)).findByDonationDateBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Should get all unique donor names")
    void testGetAllDonorNames() {
        // Given
        List<String> donorNames = Arrays.asList("Harsha", "Test 1");
        when(donationRepository.findAllUniqueDonorNames()).thenReturn(donorNames);

        // When
        List<String> result = donationService.getAllDonorNames();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Harsha"));
        assertTrue(result.contains("Test 1"));

        verify(donationRepository, times(1)).findAllUniqueDonorNames();
    }

    @Test
    @DisplayName("Should get total quantity by donor")
    void testGetTotalByDonor() {
        // Given
        BigDecimal expectedTotal = new BigDecimal("2616.08"); // 100 + 2516.08
        when(donationRepository.getTotalQuantityByDonor("Harsha")).thenReturn(expectedTotal);

        // When
        BigDecimal total = donationService.getTotalByDonor("Harsha");

        // Then
        assertNotNull(total);
        assertEquals(0, expectedTotal.compareTo(total));

        verify(donationRepository, times(1)).getTotalQuantityByDonor("Harsha");
    }

    @Test
    @DisplayName("Should return zero when donor has no donations")
    void testGetTotalByDonorNoData() {
        // Given
        when(donationRepository.getTotalQuantityByDonor("NonExistent")).thenReturn(null);

        // When
        BigDecimal total = donationService.getTotalByDonor("NonExistent");

        // Then
        assertNotNull(total);
        assertEquals(0, BigDecimal.ZERO.compareTo(total));

        verify(donationRepository, times(1)).getTotalQuantityByDonor("NonExistent");
    }

    @Test
    @DisplayName("Should get recent donations")
    void testGetRecentDonations() {
        // Given
        int days = 7;
        LocalDate sinceDate = LocalDate.now().minusDays(days);
        List<Donation> recentDonations = Arrays.asList(testDonation);

        when(donationRepository.findRecentDonations(sinceDate)).thenReturn(recentDonations);

        // When
        List<DonationResponse> responses = donationService.getRecentDonations(days);

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());

        verify(donationRepository, times(1)).findRecentDonations(sinceDate);
    }

    @Test
    @DisplayName("Should delete donation successfully")
    void testDeleteDonation() {
        // Given
        when(donationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(donationRepository).deleteById(1L);

        // When
        donationService.deleteDonation(1L);

        // Then
        verify(donationRepository, times(1)).existsById(1L);
        verify(donationRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent donation")
    void testDeleteDonationNotFound() {
        // Given
        when(donationRepository.existsById(999L)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            donationService.deleteDonation(999L);
        });

        assertTrue(exception.getMessage().contains("Donation not found"));
        verify(donationRepository, times(1)).existsById(999L);
        verify(donationRepository, never()).deleteById(999L);
    }

    @Test
    @DisplayName("Should get total donation count")
    void testGetTotalDonationCount() {
        // Given
        when(donationRepository.count()).thenReturn(10L);

        // When
        long count = donationService.getTotalDonationCount();

        // Then
        assertEquals(10L, count);
        verify(donationRepository, times(1)).count();
    }

    @Test
    @DisplayName("Should handle empty donation list")
    void testGetAllDonationsEmpty() {
        // Given
        when(donationRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<DonationResponse> responses = donationService.getAllDonations();

        // Then
        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(donationRepository, times(1)).findAll();
    }
}