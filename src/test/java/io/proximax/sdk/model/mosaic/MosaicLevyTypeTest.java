package io.proximax.sdk.model.mosaic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * {@link MosaicLevyType} tests
 */
class MosaicLevyTypeTest {
    @Test
    void checkCode() {
        // check some random item
        assertEquals(1, MosaicLevyType.LEVYABSOLUTEFEE.getValue());
    }

    @Test
    void checkGetByCode() {
        for (MosaicLevyType item : MosaicLevyType.values()) {
            assertEquals(item, MosaicLevyType.rawValueOf(item.getValue()));
        }
    }

    @Test
    void testBadCode() {
        assertThrows(RuntimeException.class, () -> MosaicLevyType.rawValueOf(-1));
    }

}
