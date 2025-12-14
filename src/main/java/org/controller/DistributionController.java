package org.controller;


import jakarta.validation.Valid;
import org.dto.DistributionRequest;
import org.dto.DistributionResponse;
import org.entity.DonationType;
import org.services.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//controller for Distribution operations
@RestController
@RequestMapping("/api/distributions")
@CrossOrigin(origins = "*")
public class DistributionController {

    private final DistributionService distributionService;

    @Autowired
    public DistributionController(DistributionService distributionService) {
        this.distributionService = distributionService;
    }

    //Record a new distribution
    @PostMapping
    public ResponseEntity<DistributionResponse> recordDistribution(@Valid @RequestBody DistributionRequest request) {
        DistributionResponse response = distributionService.recordDistribution(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //Get all distributions
    @GetMapping
    public ResponseEntity<List<DistributionResponse>> getAllDistributions() {
        List<DistributionResponse> distributions = distributionService.getAllDistributions();
        return ResponseEntity.ok(distributions);
    }

    //Get distribution by ID
    @GetMapping("/{id}")
    public ResponseEntity<DistributionResponse> getDistributionById(@PathVariable Long id) {
        DistributionResponse distribution = distributionService.getDistributionById(id);
        return ResponseEntity.ok(distribution);
    }

    //Get distributions by type
    @GetMapping("/type/{donationType}")
    public ResponseEntity<List<DistributionResponse>> getDistributionsByType(@PathVariable DonationType donationType) {
        List<DistributionResponse> distributions = distributionService.getDistributionsByType(donationType);
        return ResponseEntity.ok(distributions);
    }

    //Get distributions by recipient
    @GetMapping("/recipient/{recipient}")
    public ResponseEntity<List<DistributionResponse>> getDistributionsByRecipient(@PathVariable String recipient) {
        List<DistributionResponse> distributions = distributionService.getDistributionsByRecipient(recipient);
        return ResponseEntity.ok(distributions);
    }

    // Search distributions by recipient (recipient = who recieved the distribution , ex:XYZ client)
    @GetMapping("/search")
    public ResponseEntity<List<DistributionResponse>> searchDistributions(@RequestParam String recipient) {
        List<DistributionResponse> distributions = distributionService.searchDistributionsByRecipient(recipient);
        return ResponseEntity.ok(distributions);
    }

    //Get distributions by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<DistributionResponse>> getDistributionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DistributionResponse> distributions = distributionService.getDistributionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(distributions);
    }

    //Get recent distributions (last x days ,defaulting to 7)
    @GetMapping("/recent")
    public ResponseEntity<List<DistributionResponse>> getRecentDistributions(
            @RequestParam(defaultValue = "7") int days) {
        List<DistributionResponse> distributions = distributionService.getRecentDistributions(days);
        return ResponseEntity.ok(distributions);
    }

    //Get all unique recipients (we can know who all we distributed to clients)
    @GetMapping("/recipients")
    public ResponseEntity<List<String>> getAllRecipients() {
        List<String> recipients = distributionService.getAllRecipients();
        return ResponseEntity.ok(recipients);
    }

    //Get total distribution count
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalCount() {
        long count = distributionService.getTotalDistributionCount();
        return ResponseEntity.ok(count);
    }

    //Delete a distribution from our record by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistribution(@PathVariable Long id) {
        distributionService.deleteDistribution(id);
        return ResponseEntity.noContent().build();
    }
}