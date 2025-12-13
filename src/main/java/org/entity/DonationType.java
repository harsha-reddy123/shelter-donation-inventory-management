package org.entity;

public enum DonationType {
    MONEY("Money"),
    FOOD("Food"),
    CLOTHING("Clothing"),
    MEDICINE("Medicine"),
    BLANKETS("Blankets"),
    TOYS("Toys"),
    BOOKS("Books"),
    FURNITURE("Furniture"),
    HYGIENE_PRODUCTS("Hygiene Products"),
    OTHER("Other");

    private final String displayName;

    DonationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static DonationType fromString(String type) {
        for (DonationType donationType : DonationType.values()) {
            if (donationType.name().equalsIgnoreCase(type) ||
                    donationType.displayName.equalsIgnoreCase(type)) {
                return donationType;
            }
        }
        throw new IllegalArgumentException("Unknown donation type: " + type);
    }
}
