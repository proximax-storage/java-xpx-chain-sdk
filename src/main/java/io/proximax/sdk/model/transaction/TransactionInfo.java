/*
 * Copyright 2018 NEM
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

package io.proximax.sdk.model.transaction;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

/**
 * The transaction information model included in all transactions.
 *
 * @since 1.0
 */
public class TransactionInfo {
    private final BigInteger height;
    private final Optional<Integer> index;
    private final Optional<String> id;
    private final Optional<String> hash;
    private final Optional<String> merkleComponentHash;
    private final Optional<String> aggregateHash;
    private final Optional<String> aggregateId;
    private final Optional<String> uniqueAggregateHash;

    
    private TransactionInfo(BigInteger height, Optional<Integer> index, Optional<String> id, Optional<String> hash, Optional<String> merkleComponentHash, Optional<String> aggregateHash, Optional<String> aggregateId, Optional<String> uniqueAggregateHash) {
        Validate.notNull(height, "Height must not be null");
        this.height = height;

        this.index = index;
        this.id = id;
        this.hash = hash;
        this.merkleComponentHash = merkleComponentHash;
        this.aggregateHash = aggregateHash;
        this.aggregateId = aggregateId;
        this.uniqueAggregateHash = uniqueAggregateHash;
    }

    /**
     * Create transaction info object for aggregate transaction inner transaction.
     *
     * @param height              Block height in which the transaction was
     *                            included.
     * @param index               The transaction index.
     * @param id                  transaction id.
     * @param aggregateHash       The hash of the aggregate transaction.
     * @param aggregateId         The id of the aggregate transaction.
     * @param uniqueAggregateHash The unique hash of the aggregate transaction.
     * 
     * @return instance of TransactionInfo
     */
    public static TransactionInfo createAggregate(BigInteger height, Integer index, String id, String aggregateHash, String aggregateId, String uniqueAggregateHash) {
        return new TransactionInfo(height, Optional.of(index), Optional.of(id), Optional.empty(), Optional.empty(), Optional.of(aggregateHash), Optional.of(aggregateId), Optional.of(uniqueAggregateHash));
    }

    /**
     * Create transaction info object for a transaction.
     *
     * @param height              Block height in which the transaction was included.
     * @param index               The transaction index.
     * @param id                  transaction id.
     * @param hash                The transaction hash.
     * @param merkleComponentHash The transaction merkle component hash.
     * @return instance of TransactionInfo
     */
    public static TransactionInfo create(BigInteger height, Integer index, String id, String hash, String merkleComponentHash) {
        return new TransactionInfo(height, Optional.of(index), Optional.of(id), Optional.of(hash), Optional.of(merkleComponentHash), Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * Create transaction info retrieved by listener.
     *
     * @param height              Block height in which the transaction was included.
     * @param hash                The transaction hash
     * @param merkleComponentHash The transaction merkle component hash.
     * @return instance of TransactionInfo
     */
    public static TransactionInfo create(BigInteger height, String hash, String merkleComponentHash) {
        return new TransactionInfo(height, Optional.empty(), Optional.empty(), Optional.of(hash), Optional.of(merkleComponentHash), Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * Returns block height in which the transaction was included.
     *
     * @return block height
     */
    public BigInteger getHeight() {
        return height;
    }

    /**
     * Returns index representing either transaction index/position within block or within an aggregate transaction.
     *
     * @return optional index
     */
    public Optional<Integer> getIndex() {
        return index;
    }

    /**
     * Returns transaction id.
     *
     * @return transaction id
     */
    public Optional<String> getId() {
        return id;
    }

    /**
     * Returns transaction hash.
     *
     * @return transaction hash
     */
    public Optional<String> getHash() {
        return hash;
    }

    /**
     * Returns transaction merkle component hash.
     *
     * @return transaction merkle component hash
     */
    public Optional<String> getMerkleComponentHash() {
        return merkleComponentHash;
    }

    /**
     * Returns hash of the aggregate transaction.
     *
     * @return aggregate transaction hash
     */
    public Optional<String> getAggregateHash() {
        return aggregateHash;
    }

    /**
     * Returns id of the aggregate transaction.
     *
     * @return aggregate transaction id
     */
    public Optional<String> getAggregateId() {
        return aggregateId;
    }

    public Optional<String> getUniqueAggregateHash() {
        return uniqueAggregateHash;
    }
    
   @Override
   public int hashCode() {
      return Objects.hash(aggregateHash, aggregateId, uniqueAggregateHash,hash, height, id, index, merkleComponentHash );
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      TransactionInfo other = (TransactionInfo) obj;
        return Objects.equals(aggregateHash, other.aggregateHash) && Objects.equals(aggregateId, other.aggregateId)&& Objects.equals(uniqueAggregateHash, other.uniqueAggregateHash)&&Objects.equals(hash, other.hash) && Objects.equals(height, other.height) && Objects.equals(id, other.id) && Objects.equals(index, other.index) && Objects.equals(merkleComponentHash, other.merkleComponentHash);
   }

   @Override
   public String toString() {
      return "TransactionInfo [height=" + height + ", index=" + index + ", id=" + id + ", hash=" + hash + ", merkleComponentHash=" + merkleComponentHash + ", aggregateHash=" + aggregateHash + ", aggregateId=" + aggregateId + ", uniqueAggregateHash=" + uniqueAggregateHash +"]";
   }
    
    
}
