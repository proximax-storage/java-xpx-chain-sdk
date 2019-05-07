/*
 * Copyright 2019 ProximaX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.proximax.sdk;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import io.proximax.sdk.infrastructure.TransactionMappingTest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * base for tests that run from resource bundles
 */
public class ResourceBasedTest {

   protected JsonArray getTransactions(String bundle) {
      return getResources(bundle, "transactions", "transactions");
   }
   
   /**
    * load array of transactions from bundle with given name
    * @param name
    * @return
    */
   protected JsonArray getResources(String name, String prefix, String field) {
      // first get URI from the classpath
      URI resourceUri;
      try {
         resourceUri = TransactionMappingTest.class.getClassLoader().getResource(prefix + "/" + name + ".json")
               .toURI();
      } catch (URISyntaxException e) {
         throw new RuntimeException("Failed to locate resource " + name, e);
      }
      // load object from the classpath
      try (Stream<String> stream = Files.lines(Paths.get(resourceUri), StandardCharsets.UTF_8)) {
         StringBuilder contentBuilder = new StringBuilder();
         stream.forEach(s -> contentBuilder.append(s).append("\n"));
         JsonObject obj = new JsonObject(contentBuilder.toString());
         // retrieve transactions as an array
         return obj.getJsonArray(field);
      } catch (IOException e) {
         throw new RuntimeException("Failed to load resources from " + name, e);
      }
   }
   
   /**
    * return stream of concatenated transaction bundles
    * @param bundles
    * @return
    */
   protected Stream<JsonObject> streamTransactions(String... bundles) {
      // take bundle names and create stream of transactions in those bundles
      return Stream.of(bundles)
            // map from bundle name to stream of JsonObjects
            .map(bundle -> getTransactions(bundle).stream())
            // concatenate the streams
            .reduce(Stream::concat)
            // return empty stream if there was nothing to concatenate
            .orElse(Stream.empty())
            // map items in the stream to type JsonObject
            .map(obj -> (JsonObject) obj);
   }

}
