package org.controller;

import org.dto.DonorReportDTO;
import org.dto.InventoryReportDTO;
import org.entity.DonationType;
import org.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

//controller for Report generation
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    //Get the Current Inventory Report
    @GetMapping("/inventory")
    public ResponseEntity<InventoryReportDTO> getInventoryReport() {
        InventoryReportDTO report = reportService.generateInventoryReport();
        return ResponseEntity.ok(report);
    }

    // Get total contributions made by each donor
    @GetMapping("/donors")
    public ResponseEntity<DonorReportDTO> getDonorReport() {
        DonorReportDTO report = reportService.generateDonorReport();
        return ResponseEntity.ok(report);
    }

    /**
     Get current inventory for a specific donation type
     Returns => current stock quantity for the specified type
     */
    @GetMapping("/inventory/{donationType}")
    public ResponseEntity<BigDecimal> getInventoryByType(@PathVariable DonationType donationType) {
        BigDecimal inventory = reportService.getInventoryByType(donationType);
        return ResponseEntity.ok(inventory);
    }

    /**
     Check if sufficient inventory is available
     Returns => true if enough inventory is available for distribution
     */
    @GetMapping("/inventory/check")
    public ResponseEntity<Boolean> checkInventoryAvailability(
            @RequestParam DonationType type,
            @RequestParam BigDecimal quantity) {
        boolean available = reportService.hasAvailableInventory(type, quantity);
        return ResponseEntity.ok(available);
    }
}
