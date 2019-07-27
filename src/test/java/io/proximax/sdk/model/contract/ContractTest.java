/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.contract;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
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
      final Gson gson = new Gson();
      // deserialize bundle items
      List<Contract> items = Observable.fromIterable(getResources("contracts", "dtos", "contracts"))
            .map(JsonElement::toString)
            .map(str -> gson.fromJson(str, ContractInfoDTO.class))
            .map(ContractInfoDTO::getContract)
            .map(Contract::fromDto)
            .toList().blockingGet();
      // make sure that something was read from the bundle
      assertTrue(!items.isEmpty());
   }

}
