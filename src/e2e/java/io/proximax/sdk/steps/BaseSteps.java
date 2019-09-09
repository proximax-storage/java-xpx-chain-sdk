/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.steps;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;

import io.proximax.sdk.BaseTest;
import io.proximax.sdk.BlockchainApi;
import io.proximax.sdk.FeeCalculationStrategy;
import io.proximax.sdk.model.transaction.builder.TransactionBuilderFactory;

/**
 * TODO add proper description
 */
public class BaseSteps extends BaseTest {
   // use an hour for a transaction deadline
   protected static final BigInteger DEFAULT_DEADLINE_DURATION = BigInteger.valueOf(60*60*1000l);

   protected final BlockchainApi api;
   protected final TransactionBuilderFactory transact;

  
   public BaseSteps() throws MalformedURLException {
      String nodeUrl = this.getNodeUrl();
      // create HTTP APIs
      api = new BlockchainApi(new URL(nodeUrl), getNetworkType());
      // init the transaction builder factory
      transact = api.transact();
      transact.setDeadlineMillis(DEFAULT_DEADLINE_DURATION);
      transact.setFeeCalculationStrategy(FeeCalculationStrategy.ZERO);
   }
}
