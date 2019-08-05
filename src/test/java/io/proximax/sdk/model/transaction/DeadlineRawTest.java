/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

/**
 * {@link DeadlineRaw} tests
 */
class DeadlineRawTest {
   private static final long HOUR_MILLIS = 3_600_000l;
   private static final long ACCEPTABLE_THRESHOLD = 500l;
   @Test
   void constructor() {
      DeadlineRaw rawDeadline = new DeadlineRaw(BigInteger.valueOf(HOUR_MILLIS));
      assertEquals(HOUR_MILLIS, rawDeadline.getInstant());
   }
   
   @Test
   void startNow() {
      Deadline deadline = new Deadline(1, ChronoUnit.HOURS);
      TransactionDeadline rawDeadline = DeadlineRaw.startNow(BigInteger.valueOf(HOUR_MILLIS));
      long diff = Math.abs(deadline.getInstant() - rawDeadline.getInstant());
      assertTrue(diff < ACCEPTABLE_THRESHOLD);
   }
   
   @Test
   void compareWithDeadline() {
      Deadline deadline = new Deadline(BigInteger.valueOf(HOUR_MILLIS));
      DeadlineRaw rawDeadline = new DeadlineRaw(BigInteger.valueOf(HOUR_MILLIS));
      
      long diff = Math.abs(deadline.getInstant() - rawDeadline.getInstant());
      assertTrue(diff < ACCEPTABLE_THRESHOLD, "difference was "+diff);
   }

   @Test
   void toStringPasses() {
      DeadlineRaw rawDeadline = new DeadlineRaw(BigInteger.valueOf(HOUR_MILLIS));
      assertTrue(rawDeadline.toString().startsWith("DeadlineRaw"));
   }
}
