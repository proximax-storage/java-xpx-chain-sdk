/*
 * Copyright 2019 ProximaX Limited. All rights reserved.
 * Use of this source code is governed by the Apache 2.0
 * license that can be found in the LICENSE file.
 */
package io.proximax.sdk.model.contract;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import io.proximax.core.crypto.PublicKey;
import io.proximax.sdk.gen.model.ContractDTO;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.utils.dto.UInt64Utils;

/**
 * contract descriptor
 */
public class Contract {

   private final String multisig;
   private final Address multisigAddress;
   private final BigInteger start;
   private final BigInteger duration;
   private final String contentHash;
   private final List<ContractContentHashRecord> contentHashRecords;
   private final List<PublicKey> customers;
   private final List<PublicKey> executors;
   private final List<PublicKey> verifiers;

   /**
    * Create new contract instance
    * 
    * @param multisig
    * @param multisigAddress
    * @param start
    * @param duration
    * @param contentHash
    * @param customers
    * @param executors
    * @param verifiers
    */
   public Contract(String multisig, Address multisigAddress, BigInteger start, BigInteger duration, String contentHash, List<ContractContentHashRecord> contentHashRecords,
         List<PublicKey> customers, List<PublicKey> executors, List<PublicKey> verifiers) {
      this.multisig = multisig;
      this.multisigAddress = multisigAddress;
      this.start = start;
      this.duration = duration;
      this.contentHash = contentHash;
      this.contentHashRecords = contentHashRecords;
      this.customers = customers;
      this.executors = executors;
      this.verifiers = verifiers;
   }

   /**
    * @return the multisig
    */
   public String getMultisig() {
      return multisig;
   }

   /**
    * @return the multisigAddress
    */
   public Address getMultisigAddress() {
      return multisigAddress;
   }

   /**
    * @return the start
    */
   public BigInteger getStart() {
      return start;
   }

   /**
    * @return the duration
    */
   public BigInteger getDuration() {
      return duration;
   }

   /**
    * @return the contentHash
    */
   public String getContentHash() {
      return contentHash;
   }

   /**
    * @return the contentHashRecords
    */
   public List<ContractContentHashRecord> getContentHashRecords() {
      return contentHashRecords;
   }

   /**
    * @return the customers
    */
   public List<PublicKey> getCustomers() {
      return customers;
   }

   /**
    * @return the executors
    */
   public List<PublicKey> getExecutors() {
      return executors;
   }

   /**
    * @return the verifiers
    */
   public List<PublicKey> getVerifiers() {
      return verifiers;
   }

   
   @Override
   public int hashCode() {
      return Objects.hash(contentHash, customers, duration, executors, multisig, multisigAddress, start, verifiers);
   }

   
   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Contract other = (Contract) obj;
      return Objects.equals(contentHash, other.contentHash) && Objects.equals(customers, other.customers)
            && Objects.equals(duration, other.duration) && Objects.equals(executors, other.executors)
            && Objects.equals(multisig, other.multisig) && Objects.equals(multisigAddress, other.multisigAddress)
            && Objects.equals(start, other.start) && Objects.equals(verifiers, other.verifiers);
   }

   /**
    * create contract instance form the DTO
    * 
    * @param dto the DT that is to be processed
    * @return the contract instance
    */
   public static Contract fromDto(ContractDTO dto) {
      return new Contract(
            dto.getMultisig(),
            Address.createFromEncoded(dto.getMultisigAddress()),
            UInt64Utils.toBigInt(dto.getStart()),
            UInt64Utils.toBigInt(dto.getDuration()),
            dto.getHash(),
            Optional.ofNullable(dto.getHashes()).orElse(Collections.emptyList()).stream()
                  .map(ContractContentHashRecord::fromDto)
                  .collect(Collectors.toList()),
            getPublicKeys(dto.getCustomers()),
            getPublicKeys(dto.getExecutors()),
            getPublicKeys(dto.getVerifiers()));
   }

   /**
    * take list of hexadecimal strings representing public keys and convert it to list of public keys
    * 
    * @param strings list hexadecimal strings
    * @return list of public keys
    */
   private static List<PublicKey> getPublicKeys(List<String> strings) {
      return strings.stream().map(PublicKey::fromHexString).collect(Collectors.toList());
   }
}
