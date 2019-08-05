/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.transaction;

import java.math.BigInteger;
import java.util.Date;

/**
 * Raw deadline implementation working directly with milliseconds. Consider using {@link Deadline} or {@link DeadlineBP}
 */
public class DeadlineRaw implements TransactionDeadline {

   /** milliseconds since epoch */
   private final long deadline;

   /**
    * @param deadline in milliseconds since epoch
    */
   public DeadlineRaw(BigInteger deadline) {
      this.deadline = deadline.longValue();
   }

   /**
    * create new deadline instance specifying duration since current time
    * 
    * @param duration duration after current time
    * @return deadline representing specified time
    */
   public static TransactionDeadline startNow(BigInteger duration) {
      return new DeadlineRaw(BigInteger
            .valueOf(System.currentTimeMillis() - TransactionDeadline.NETWORK_EPOCH_START_MILLIS).add(duration));
   }

   @Override
   public long getInstant() {
      return deadline;
   }

   @Override
   public String toString() {
      return "DeadlineRaw [deadline=" + new Date(deadline + TransactionDeadline.NETWORK_EPOCH_START_MILLIS) + "]";
   }

   
}
