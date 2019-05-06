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

import org.junit.jupiter.api.Test;

import io.proximax.sdk.model.blockchain.BlockchainStorageInfo;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockchainStorageInfoTest {

    @Test
    void createANewBlockchainStorageInfo() {
        BlockchainStorageInfo blockchainStorageInfo = new BlockchainStorageInfo(1,2,3);

        assertTrue(blockchainStorageInfo.getNumAccounts() == 1);
        assertTrue(blockchainStorageInfo.getNumBlocks() == 2);
        assertTrue(blockchainStorageInfo.getNumTransactions() == 3);
    }
}