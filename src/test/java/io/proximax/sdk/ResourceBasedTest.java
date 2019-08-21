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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.proximax.core.utils.StringUtils;
import io.proximax.sdk.utils.GsonUtils;

/**
 * base for tests that run from resource bundles
 */
public class ResourceBasedTest {

   protected JsonArray getTransactions(String bundle) {
      return getResources(bundle, "transactions", "transactions");
   }

   /**
    * load array of transactions from bundle with given name
    * 
    * @param name
    * @return
    */
   protected JsonArray getResources(String name, String prefix, String field) {
      // first get URI from the classpath
      URI resourceUri;
      try {
         resourceUri = ResourceBasedTest.class.getClassLoader().getResource(prefix + "/" + name + ".json").toURI();
      } catch (URISyntaxException e) {
         throw new RuntimeException("Failed to locate resource " + name, e);
      }
      // load object from the classpath
      try (Stream<String> stream = Files.lines(Paths.get(resourceUri), StandardCharsets.UTF_8)) {
         StringBuilder contentBuilder = new StringBuilder();
         stream.forEach(s -> contentBuilder.append(s).append("\n"));
         JsonObject obj = GsonUtils.mapToJsonObject(contentBuilder.toString());
         // retrieve transactions as an array
         return obj.get(field).getAsJsonArray();
      } catch (IOException e) {
         throw new RuntimeException("Failed to load resources from " + name, e);
      }
   }

   /**
    * return stream of concatenated transaction bundles
    * 
    * @param bundles
    * @return
    */
   protected Stream<JsonObject> streamTransactions(String... bundles) {
      // take bundle names and create stream of transactions in those bundles
      return Stream.of(bundles)
            // map from bundle name to stream of JsonObjects
            .map(bundle -> GsonUtils.stream(getTransactions(bundle)))
            // concatenate the streams
            .reduce(Stream::concat)
            // return empty stream if there was nothing to concatenate
            .orElse(Stream.empty())
            // map items in the stream to type JsonObject
            .map(obj -> (JsonObject) obj);
   }

   /**
    * save bytes to serialization folder in test resources. This is intended to build tests
    * 
    * @param name name of the resource
    * @param bytes bytes to save
    * @return the created file
    * @throws FileNotFoundException if directory does not exist (it should...)
    * @throws IOException if write failed
    */
   public static File saveBytes(String name, byte[] bytes) throws IOException {
      File dest = new File("src/test/resources/serialization/" + name + ".bytes");
      try (FileOutputStream fos = new FileOutputStream(dest)) {
         fos.write(bytes);
      }
      return dest;
   }

   public static byte[] loadBytes(String name) throws IOException {
      try (InputStream is = ResourceBasedTest.class.getClassLoader()
            .getResource("serialization/" + name + ".bytes").openStream()) {
         return IOUtils.toByteArray(is);
      }
   }
   
   /**
    * read resource on a classpath to a String
    * 
    * @param name the name f the resource
    * @return the String with contents
    * @throws IOException on error
    */
   public static String getResourceAsString(String name) throws IOException {
      try (InputStream is = ResourceBasedTest.class.getClassLoader().getResourceAsStream(name)) {
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         byte[] buffer = new byte[1024];
         int size;
         while ((size = is.read(buffer)) >= 0) {
            bos.write(buffer, 0, size);
         }
         return StringUtils.getString(bos.toByteArray());
      }
   }
}
