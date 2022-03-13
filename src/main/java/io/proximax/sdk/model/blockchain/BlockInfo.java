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

package io.proximax.sdk.model.blockchain;

import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

import io.proximax.sdk.gen.model.BlockInfoDTO;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.network.NetworkType;

/**
 * The block info structure describes basic information of a block.
 *
 * @since 1.0
 */
public class BlockInfo {
    private final String hash;
    private final String generationHash;
    private final Optional<BigInteger> totalFee;
    private final Optional<Integer> numTransactions;
    private final String signature;
    private final PublicAccount signer;
    private final NetworkType networkType;
    private final Integer version;
    private final int type;
    private final BigInteger height;
    private final BigInteger timestamp;
    private final BigInteger difficulty;
    private final String previousBlockHash;
    private final String blockTransactionsHash;
    private final Optional<String> blockReceiptsHash;


   public BlockInfo(String hash, String generationHash, Optional<BigInteger> totalFee,
         Optional<Integer> numTransactions, String signature, PublicAccount signer, NetworkType networkType,
         Integer version, int type, BigInteger height, BigInteger timestamp, BigInteger difficulty,
         String previousBlockHash, String blockTransactionsHash, Optional<String> blockReceiptsHash) {
        this.hash = hash;
        this.generationHash = generationHash;
        this.totalFee = totalFee;
        this.numTransactions = numTransactions;
        this.signature = signature;
        this.signer = signer;
        this.networkType = networkType;
        this.version = version;
        this.type = type;
        this.height = height;
        this.timestamp = timestamp;
        this.difficulty = difficulty;
        this.previousBlockHash = previousBlockHash;
        this.blockTransactionsHash = blockTransactionsHash;
        this.blockReceiptsHash = blockReceiptsHash;
    }

    /**
    * @return the blockReceiptsHash
    */
   public Optional<String> getBlockReceiptsHash() {
      return blockReceiptsHash;
   }

   /**
     * Returns block hash.
     *
     * @return String
     */
    public String getHash() {
        return hash;
    }

    /**
     * Returns block generation hash.
     *
     * @return String
     */
    public String getGenerationHash() {
        return generationHash;
    }

    /**
     * Returns total fee paid to the account harvesting the block. When generated by listeners optional empty.
     *
     * @return Optional of Integer
     */
    public Optional<BigInteger> getTotalFee() {
        return totalFee;
    }

    /**
     * Returns number of transactions included the block. When generated by listeners optional empty.
     *
     * @return Optional of Integer
     */
    public Optional<Integer> getNumTransactions() {
        return numTransactions;
    }

    /**
     * Returns The block signature.
     * The signature was generated by the signer and can be used to validate that the blockchain data was not modified by a node.
     *
     * @return String
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Returns public account of block harvester.
     *
     * @return {@link PublicAccount}
     */
    public PublicAccount getSigner() {
        return signer;
    }

    /**
     * Returns network type.
     *
     * @return {@link NetworkType}
     */
    public NetworkType getNetworkType() {
        return networkType;
    }

    /**
     * Returns block transaction version.
     *
     * @return Integer
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Returns block transaction type.
     *
     * @return int
     */
    public int getType() {
        return type;
    }

    /**
     * Returns height of which the block was confirmed. Each block has a unique height. Subsequent blocks differ in height by 1.
     *
     * @return BigInteger
     */
    public BigInteger getHeight() {
        return height;
    }

    /**
     * Returns the number of seconds elapsed since the creation of the nemesis blockchain.
     *
     * @return BigInteger
     */
    public BigInteger getTimestamp() {
        return timestamp;
    }

    /**
     * Returns POI difficulty to harvest a block.
     *
     * @return BigInteger
     */
    public BigInteger getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the last block hash.
     *
     * @return String
     */
    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    /**
     * Returns the block transaction hash.
     *
     * @return String
     */
    public String getBlockTransactionsHash() {
        return blockTransactionsHash;
    }

    /**
     * create new block info from the DTO
     * 
     * @param dto the DTO as received form the server
     * @param networkType network type
     * @return the block info represented by the DTO
     */
    public static BlockInfo fromDto(BlockInfoDTO dto, NetworkType networkType) {
       return new BlockInfo(dto.getMeta().getHash(),
             dto.getMeta().getGenerationHash(),
             Optional.of(toBigInt(new ArrayList<>(dto.getMeta().getTotalFee()))),
             Optional.of(dto.getMeta().getNumTransactions()),
             dto.getBlock().getSignature(),
             new PublicAccount(dto.getBlock().getSigner(), networkType),
             networkType,
             (int) Long.parseLong(Integer.toHexString(dto.getBlock().getVersion()).substring(2, 4), 16),
             dto.getBlock().getType().getValue(),
             toBigInt(new ArrayList<>(dto.getBlock().getHeight())),
             toBigInt(new ArrayList<>(dto.getBlock().getTimestamp())),
             toBigInt(new ArrayList<>(dto.getBlock().getDifficulty())),
             dto.getBlock().getPreviousBlockHash(),
             dto.getBlock().getBlockTransactionsHash(),
             Optional.of(dto.getBlock().getBlockReceiptsHash()));
    }

   @Override
   public String toString() {
      return "BlockInfo [hash=" + hash + ", generationHash=" + generationHash + ", totalFee=" + totalFee
            + ", numTransactions=" + numTransactions + ", signature=" + signature + ", signer=" + signer
            + ", networkType=" + networkType + ", version=" + version + ", type=" + type + ", height=" + height
            + ", timestamp=" + timestamp + ", difficulty=" + difficulty + ", previousBlockHash=" + previousBlockHash
            + ", blockTransactionsHash=" + blockTransactionsHash + ", blockReceiptsHash=" + blockReceiptsHash + "]";
   }
    
}
