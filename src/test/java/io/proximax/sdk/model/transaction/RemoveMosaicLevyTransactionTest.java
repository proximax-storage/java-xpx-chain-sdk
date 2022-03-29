package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.network.NetworkType;

public class RemoveMosaicLevyTransactionTest extends ResourceBasedTest{
    @Test
    void createARemoveMosaicLevyTransactionViaConstructor() {
        RemoveMosaicLevyTransaction removeMosaicLevyTransaction = new RemoveMosaicLevyTransaction(NetworkType.TEST_NET,
                1, new Deadline(2, ChronoUnit.HOURS), BigInteger.ZERO, Optional.empty(),
                Optional.empty(), Optional.empty(), new MosaicId("34b40b8ad0cee3f3"));

        assertEquals(NetworkType.TEST_NET, removeMosaicLevyTransaction.getNetworkType());
        assertTrue(1 == removeMosaicLevyTransaction.getVersion());
        long nowSinceNemesis = new Deadline(0, ChronoUnit.SECONDS).getInstant();
        assertTrue(nowSinceNemesis < removeMosaicLevyTransaction.getDeadline().getInstant());
        assertEquals(BigInteger.valueOf(0), removeMosaicLevyTransaction.getMaxFee());
        assertEquals("34b40b8ad0cee3f3", removeMosaicLevyTransaction.getMosaicId().getIdAsHex());
    }
    
    // @Test
    // @DisplayName("Serialization")
    // void serialization() throws IOException {
    //           RemoveMosaicLevyTransaction removeMosaicLevyTransaction = new RemoveMosaicLevyTransaction(NetworkType.TEST_NET,
    //             1, new Deadline(2, ChronoUnit.HOURS), BigInteger.ZERO, Optional.empty(),
    //             Optional.empty(), Optional.empty(), new MosaicId("34b40b8ad0cee3f3"));
    //           byte[] actual = removeMosaicLevyTransaction.generateBytes();
    //           //assertEquals(loadBytes("modify_mosic_levy"), actual);

    // }
}
