package org.mapper;

import org.dto.DistributionRequest;
import org.dto.DistributionResponse;
import org.entity.Distribution;

import java.time.LocalDate;

// Mapper to convert between Distribution Entity and DTOs - - for seperable logic
public class DistributionMapper {

    //convert DistributionRequest DTO to Distribution Entity
    public static Distribution toEntity(DistributionRequest request) {
        Distribution distribution = new Distribution();
        distribution.setDonationType(request.getDonationType());
        distribution.setQuantity(request.getQuantity());
        distribution.setRecipient(request.getRecipient());

        // store provided date or default to today
        distribution.setDistributionDate(
                request.getDistributionDate() != null ? request.getDistributionDate() : LocalDate.now()
        );

        return distribution;
    }

    // Convert Distribution Entity to DistributionResponse DTO
    public static DistributionResponse toResponse(Distribution distribution) {
        return new DistributionResponse(
                distribution.getId(),
                distribution.getDonationType(),
                distribution.getQuantity(),
                distribution.getDistributionDate(),
                distribution.getRecipient(),
                distribution.getCreatedAt()
        );
    }
}