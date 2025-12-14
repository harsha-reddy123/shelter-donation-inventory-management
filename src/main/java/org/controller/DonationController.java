package org.controller;

import java.lang.System;
import jakarta.validation.Valid;
import org.dto.DonationRequest;
import org.dto.DonationResponse;
import org.entity.DonationType;
import org.services.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//controller for Donation operations
@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "*") // CORS across browsers
public class DonationController {

    private final DonationService donationService;

    @Autowired
    public DonationController(DonationService donationService) {
        this.donationService = donationService;
        System.out.println("donationController invoked");
    }

    // Register a new donation
    @PostMapping
    public ResponseEntity<DonationResponse> registerDonation(@Valid @RequestBody DonationRequest request) {
        DonationResponse response = donationService.registerDonation(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get all donations
    @GetMapping
    public ResponseEntity<List<DonationResponse>> getAllDonations() {
        System.out.println("DonationController getAllDonations invoked");
        List<DonationResponse> donations = donationService.getAllDonations();
        return ResponseEntity.ok(donations);
    }

    //Get donation by ID
    @GetMapping("/{id}")
    public ResponseEntity<DonationResponse> getDonationById(@PathVariable Long id) {
        DonationResponse donation = donationService.getDonationById(id);
        return ResponseEntity.ok(donation);
    }

    //Get donations by donor name
    @GetMapping("/donor/{donorName}")
    public ResponseEntity<List<DonationResponse>> getDonationsByDonor(@PathVariable String donorName) {
        List<DonationResponse> donations = donationService.getDonationsByDonor(donorName);
        return ResponseEntity.ok(donations);
    }

    //Get donations by type
    @GetMapping("/type/{donationType}")
    public ResponseEntity<List<DonationResponse>> getDonationsByType(@PathVariable DonationType donationType) {
        List<DonationResponse> donations = donationService.getDonationsByType(donationType);
        return ResponseEntity.ok(donations);
    }

    // Search donations by donor name (ex: name ='harsha'-string)
    @GetMapping("/search")
    public ResponseEntity<List<DonationResponse>> searchDonations(@RequestParam String donor) {
        List<DonationResponse> donations = donationService.searchDonationsByDonor(donor);
        return ResponseEntity.ok(donations);
    }

    //Get donations by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<DonationResponse>> getDonationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DonationResponse> donations = donationService.getDonationsByDateRange(startDate, endDate);
        return ResponseEntity.ok(donations);
    }

    // Get recent donations (last x days ,defaulting to 7)
    @GetMapping("/recent")
    public ResponseEntity<List<DonationResponse>> getRecentDonations(
            @RequestParam(defaultValue = "7") int days) {
        List<DonationResponse> donations = donationService.getRecentDonations(days);
        return ResponseEntity.ok(donations);
    }

    // Get all unique donor names
    @GetMapping("/donors")
    public ResponseEntity<List<String>> getAllDonorNames() {
        List<String> donors = donationService.getAllDonorNames();
        return ResponseEntity.ok(donors);
    }

    //Get total donation count
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalCount() {
        long count = donationService.getTotalDonationCount();
        return ResponseEntity.ok(count);
    }

    // Delete a donation by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable Long id) {
        donationService.deleteDonation(id);
        return ResponseEntity.noContent().build();
    }
}
