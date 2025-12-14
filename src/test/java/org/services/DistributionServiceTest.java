package org.services;


import org.dto.DistributionRequest;
import org.entity.Distribution;
import org.entity.DonationType;
import org.dto.DistributionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.repository.DistributionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//Unit tests for DistributionService
@ExtendWith(MockitoExtension.class)
class DistributionServiceTest {

    @Mock
    private DistributionRepository distributionRepository;

    @InjectMocks
    private DistributionService distributionService;

    private Distribution testDistribution;
    private DistributionRequest testRequest;

    @BeforeEach
    void setUp() {
        // Create test distribution entity
        testDistribution = new Distribution(
                DonationType.FOOD,
                new BigDecimal("50.00"),
                LocalDate.of(2025, 12, 13)
        );
        testDistribution.setId(1L);
        testDistribution.setRecipient("Food Distribution 1");
        testDistribution.setCreatedAt(LocalDateTime.now());

        // Create test distribution request
        testRequest = new DistributionRequest(
                DonationType.FOOD,
                new BigDecimal("50.00"),
                LocalDate.of(2025, 12, 13),
                "Food Distribution 1"
        );
    }

    @Test
    @DisplayName("Should record distribution successfully")
    void testRecordDistribution() {
        // Given
        when(distributionRepository.save(any(Distribution.class))).thenReturn(testDistribution);

        // When
        DistributionResponse response = distributionService.recordDistribution(testRequest);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(DonationType.FOOD, response.getDonationType());
        assertEquals(0, new BigDecimal("50.00").compareTo(response.getQuantity()));
        assertEquals("Food Distribution 1", response.getRecipient());

        verify(distributionRepository, times(1)).save(any(Distribution.class));
    }

    @Test
    @DisplayName("Should get all distributions")
    void testGetAllDistributions() {
        // Given
        Distribution distribution2 = new Distribution(
                DonationType.CLOTHING,
                new BigDecimal("100.00"),
                LocalDate.now()
        );
        distribution2.setId(2L);
        distribution2.setRecipient("Food Distribution 2");
        distribution2.setCreatedAt(LocalDateTime.now());

        List<Distribution> distributions = Arrays.asList(testDistribution, distribution2);
        when(distributionRepository.findAll()).thenReturn(distributions);

        // When
        List<DistributionResponse> responses = distributionService.getAllDistributions();

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Food Distribution 1", responses.get(0).getRecipient());
        assertEquals("Food Distribution 2", responses.get(1).getRecipient());

        verify(distributionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get distribution by ID")
    void testGetDistributionById() {
        // Given
        when(distributionRepository.findById(1L)).thenReturn(Optional.of(testDistribution));

        // When
        DistributionResponse response = distributionService.getDistributionById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(DonationType.FOOD, response.getDonationType());
        assertEquals("Food Distribution 1", response.getRecipient());

        verify(distributionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when distribution not found by ID")
    void testGetDistributionByIdNotFound() {
        // Given
        when(distributionRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            distributionService.getDistributionById(999L);
        });

        assertTrue(exception.getMessage().contains("Distribution not found"));
        verify(distributionRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get distributions by type")
    void testGetDistributionsByType() {
        // Given
        List<Distribution> foodDistributions = Arrays.asList(testDistribution);
        when(distributionRepository.findByDonationType(DonationType.FOOD)).thenReturn(foodDistributions);

        // When
        List<DistributionResponse> responses = distributionService.getDistributionsByType(DonationType.FOOD);

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(DonationType.FOOD, responses.get(0).getDonationType());

        verify(distributionRepository, times(1)).findByDonationType(DonationType.FOOD);
    }


    @Test
    @DisplayName("Should search distributions by recipient")
    void testSearchDistributionsByRecipient() {
        // Given
        List<Distribution> searchResults = Arrays.asList(testDistribution);
        when(distributionRepository.findByRecipientContainingIgnoreCase("Food Distribution"))
                .thenReturn(searchResults);

        // When
        List<DistributionResponse> responses = distributionService.searchDistributionsByRecipient("Food Distribution");

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertTrue(responses.get(0).getRecipient().toLowerCase().contains("Food Distribution"));

        verify(distributionRepository, times(1)).findByRecipientContainingIgnoreCase("Food Distribution");
    }

    @Test
    @DisplayName("Should get distributions by date range")
    void testGetDistributionsByDateRange() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 12, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        List<Distribution> distributionsInRange = Arrays.asList(testDistribution);

        when(distributionRepository.findByDistributionDateBetween(startDate, endDate))
                .thenReturn(distributionsInRange);

        // When
        List<DistributionResponse> responses = distributionService.getDistributionsByDateRange(startDate, endDate);

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());

        verify(distributionRepository, times(1)).findByDistributionDateBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Should get all unique recipients")
    void testGetAllRecipients() {
        // Given
        List<String> recipients = Arrays.asList("Food Distribution A", "Food Distribution B", "Community Center");
        when(distributionRepository.findAllUniqueRecipients()).thenReturn(recipients);

        // When
        List<String> result = distributionService.getAllRecipients();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("Food Distribution A"));
        assertTrue(result.contains("Food Distribution B"));
        assertTrue(result.contains("Community Center"));

        verify(distributionRepository, times(1)).findAllUniqueRecipients();
    }

    @Test
    @DisplayName("Should get total quantity by type")
    void testGetTotalByType() {
        // Given
        BigDecimal expectedTotal = new BigDecimal("150.00");
        when(distributionRepository.getTotalQuantityByType(DonationType.FOOD)).thenReturn(expectedTotal);

        // When
        BigDecimal total = distributionService.getTotalByType(DonationType.FOOD);

        // Then
        assertNotNull(total);
        assertEquals(0, expectedTotal.compareTo(total));

        verify(distributionRepository, times(1)).getTotalQuantityByType(DonationType.FOOD);
    }

    @Test
    @DisplayName("Should return zero when no distributions for type")
    void testGetTotalByTypeNoData() {
        // Given
        when(distributionRepository.getTotalQuantityByType(DonationType.TOYS)).thenReturn(null);

        // When
        BigDecimal total = distributionService.getTotalByType(DonationType.TOYS);

        // Then
        assertNotNull(total);
        assertEquals(0, BigDecimal.ZERO.compareTo(total));

        verify(distributionRepository, times(1)).getTotalQuantityByType(DonationType.TOYS);
    }

    @Test
    @DisplayName("Should get recent distributions")
    void testGetRecentDistributions() {
        // Given
        int days = 7;
        LocalDate sinceDate = LocalDate.now().minusDays(days);
        List<Distribution> recentDistributions = Arrays.asList(testDistribution);

        when(distributionRepository.findRecentDistributions(sinceDate)).thenReturn(recentDistributions);

        // When
        List<DistributionResponse> responses = distributionService.getRecentDistributions(days);

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());

        verify(distributionRepository, times(1)).findRecentDistributions(sinceDate);
    }

    @Test
    @DisplayName("Should delete distribution successfully")
    void testDeleteDistribution() {
        // Given
        when(distributionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(distributionRepository).deleteById(1L);

        // When
        distributionService.deleteDistribution(1L);

        // Then
        verify(distributionRepository, times(1)).existsById(1L);
        verify(distributionRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent distribution")
    void testDeleteDistributionNotFound() {
        // Given
        when(distributionRepository.existsById(999L)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            distributionService.deleteDistribution(999L);
        });

        assertTrue(exception.getMessage().contains("Distribution not found"));
        verify(distributionRepository, times(1)).existsById(999L);
        verify(distributionRepository, never()).deleteById(999L);
    }

    @Test
    @DisplayName("Should get total distribution count")
    void testGetTotalDistributionCount() {
        // Given
        when(distributionRepository.count()).thenReturn(5L);

        // When
        long count = distributionService.getTotalDistributionCount();

        // Then
        assertEquals(5L, count);
        verify(distributionRepository, times(1)).count();
    }

    @Test
    @DisplayName("Should handle empty distribution list")
    void testGetAllDistributionsEmpty() {
        // Given
        when(distributionRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<DistributionResponse> responses = distributionService.getAllDistributions();

        // Then
        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(distributionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should record distribution without recipient")
    void testRecordDistributionWithoutRecipient() {
        // Given
        DistributionRequest requestWithoutRecipient = new DistributionRequest(
                DonationType.MONEY,
                new BigDecimal("1000.00"),
                LocalDate.now(),
                null
        );

        Distribution distributionWithoutRecipient = new Distribution(
                DonationType.MONEY,
                new BigDecimal("1000.00"),
                LocalDate.now()
        );
        distributionWithoutRecipient.setId(3L);
        distributionWithoutRecipient.setCreatedAt(LocalDateTime.now());

        when(distributionRepository.save(any(Distribution.class))).thenReturn(distributionWithoutRecipient);

        // When
        DistributionResponse response = distributionService.recordDistribution(requestWithoutRecipient);

        // Then
        assertNotNull(response);
        assertEquals(DonationType.MONEY, response.getDonationType());
        assertNull(response.getRecipient());

        verify(distributionRepository, times(1)).save(any(Distribution.class));
    }
}