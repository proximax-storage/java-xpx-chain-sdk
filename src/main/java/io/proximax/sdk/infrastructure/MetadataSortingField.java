

package io.proximax.sdk.infrastructure;

public enum MetadataSortingField {
    VALUE("metadataEntry.value"),
    VALUE_SIZE("metadataEntry.valueSize");

    private String value;

    MetadataSortingField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static MetadataSortingField fromValue(String value) {
        for (MetadataSortingField b : MetadataSortingField.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

}
