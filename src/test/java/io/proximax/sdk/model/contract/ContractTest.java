/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.contract;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;

import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.gen.model.ContractInfoDTO;
import io.reactivex.Observable;

/**
 * contract tests
 */
class ContractTest extends ResourceBasedTest {

   @Test
   void testDeserialization() {
      // get the object mapper
      final ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      // deserialize bundle items
      Long items = Observable.fromIterable(getResources("contracts", "dtos", "contracts"))
            .map(JsonElement::toString)
            .map(str -> objectMapper.readValue(str, ContractInfoDTO.class))
            .map(ContractInfoDTO::getContract)
            .map(Contract::fromDto)
            .count()
            .blockingGet();
      // make sure that something was read from the bundle
      assertTrue(items > 0);
   }

}
