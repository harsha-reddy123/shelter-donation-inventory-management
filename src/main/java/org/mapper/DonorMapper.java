package org.mapper;


import org.dto.DonationRequest;
import org.dto.DonationResponse;
import org.entity.Donation;

import java.time.LocalDate;

// Mapper to convert between Donation Entity and DTOs - for seperable logic
public class DonorMapper {

    // convert DonationRequest DTO to Donation Entity
    public static Donation toEntity(DonationRequest request) {
        Donation donation = new Donation();
        donation.setDonorName(request.getDonorName());
        donation.setDonationType(request.getDonationType());
        donation.setQuantity(request.getQuantity());

        // store provided date or default to today
        donation.setDonationDate(
                request.getDonationDate() != null ? request.getDonationDate() : LocalDate.now()
        );

        return donation;
    }

    // convert Donation Entity to DonationResponse DTO
    public static DonationResponse toResponse(Donation donation) {
        return new DonationResponse(
                donation.getId(),
                donation.getDonorName(),
                donation.getDonationType(),
                donation.getQuantity(),
                donation.getDonationDate(),
                donation.getCreatedAt()
        );
    }
}