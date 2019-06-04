/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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
      List<Contract> items = Observable.fromIterable(getResources("contracts", "dtos", "contracts"))
            .map(JsonElement::toString).map(str -> objectMapper.readValue(str, ContractInfoDTO.class))
            .map(ContractInfoDTO::getContract).map(Contract::fromDto).toList().blockingGet();
      // make sure that something was read from the bundle
      assertTrue(!items.isEmpty());
      // check for specific data
      assertEquals(2,
            items.stream()
                  .filter(contract -> contract.getMultisig()
                        .equals("E0EA0A76100DE79C1693653E562542EE1DC791F447686AE82647712A2C42AA32"))
                  .findFirst()
                  .orElseThrow(() -> new RuntimeException("item missing"))
                  .getContentHashRecords().size());
   }

}
