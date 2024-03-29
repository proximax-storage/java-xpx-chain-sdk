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

package io.proximax.sdk.infrastructure;

import static io.proximax.sdk.utils.GsonUtils.getFieldOfObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.spongycastle.util.encoders.Hex;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import io.proximax.core.crypto.KeyPair;
import io.proximax.core.crypto.PrivateKey;
import io.proximax.core.crypto.PublicKey;
import io.proximax.sdk.ResourceBasedTest;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.namespace.NamespaceType;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.model.transaction.*;
import io.proximax.sdk.utils.GsonUtils;
import io.proximax.sdk.utils.dto.TransactionMappingUtils;
import io.proximax.sdk.utils.dto.UInt64Utils;
public class TransactionMappingTest extends ResourceBasedTest {

    @Test
    void validateStandaloneTransactions() {
    	streamTransactions("all")
    		.forEachOrdered(transactionDTO -> {
    			Transaction transaction = new TransactionMapping().apply(transactionDTO);
    			validateStandaloneTransaction(transaction, transactionDTO);
    		}
    	);
    }

    @Test
    void testMessages() {
    	// Sending transactions to Account [keyPair=KeyPair [privateKey=6556da78c063e0547b7fd2e8a8b66ba09b8f28043235fea441414f0fc591f507, publicKey=831b324ae7248acd22d1d0b463e19b427664c00255c6df3050345054c1343557], publicAccount=PublicAccount [address=Address [address=VDNPRW6PPFLIX64XUOQVQCF6RQ3D5E7LZWY5APYV, networkType=TEST_NET], publicKey=831B324AE7248ACD22D1D0B463E19B427664C00255C6DF3050345054C1343557]]
    	streamTransactions("TRANSFER.messages")
    		.forEachOrdered(transactionDTO -> {
    			Transaction transaction = new TransactionMapping().apply(transactionDTO);
    			validateStandaloneTransaction(transaction, transactionDTO);
    			// assert transfer
    			assertEquals(EntityType.TRANSFER, transaction.getType());
    			// retrieve the message
    			Message msg = ((TransferTransaction)transaction).getMessage();
    			MessageType msgType = MessageType.getByCode(msg.getTypeCode());
    			switch (msgType) {
    				case PLAIN:
    					// this message comes from the integration test
    					assertEquals("java SDK plain message test", ((PlainMessage)msg).getPayload());
    					break;
    				case SECURE:
    					SecureMessage secMsg = (SecureMessage)msg;
    					// private key of recipient is needed to decrypt the message
    					KeyPair recipientKeyPair = new KeyPair(PrivateKey.fromHexString("530f670b492c2949ebe9bd70443e6b9c3bd8a036ceffe717b69d197643da954a"));
    					// public key of sender is needed to decrypt the message
    					KeyPair senderKeyPair = new KeyPair(PublicKey.fromHexString("0eb448d07c7ccb312989ac27aa052738ff589e2f83973f909b506b450dc5c4e2"));
    					// check that all went well
    					assertEquals("java SDK secure message", secMsg.decrypt(recipientKeyPair, senderKeyPair));
    					break;
    				case UNKNOWN: fail("Unsupported message type " + msgType);
    			}
    		}
    	);
    }

    @Test
    void testExtractInfoFromVersion() {
       // this constant was generated by Transaction#getTxVersionforSerialization()
       int version = -1879048191;
       JsonElement versionElem = new JsonPrimitive(version);
       assertEquals(NetworkType.TEST_NET, TransactionMappingUtils.extractNetworkType(versionElem));
       assertEquals(1, TransactionMappingUtils.extractTransactionVersion(versionElem));
    }
    
    private void validateStandaloneTransaction(Transaction transaction, JsonObject transactionDTO) {
        assertEquals(extractBigInteger(getFieldOfObject(transactionDTO, "meta", "height").getAsJsonArray()),
                transaction.getTransactionInfo().get().getHeight());
        if (transaction.getTransactionInfo().get().getHash().isPresent()) {
            assertEquals(getFieldOfObject(transactionDTO, "meta", "hash").getAsString(),
                    transaction.getTransactionInfo().get().getHash().get());
        }
        if (transaction.getTransactionInfo().get().getMerkleComponentHash().isPresent()) {
            assertEquals(getFieldOfObject(transactionDTO, "meta", "merkleComponentHash").getAsString(),
                    transaction.getTransactionInfo().get().getMerkleComponentHash().get());
        }
        if (transaction.getTransactionInfo().get().getIndex().isPresent()) {
            assertEquals(transaction.getTransactionInfo().get().getIndex().get(),
                  getFieldOfObject(transactionDTO, "meta", "index").getAsInt());
        }
        if (transaction.getTransactionInfo().get().getId().isPresent()) {
            assertEquals(getFieldOfObject(transactionDTO, "meta", "id").getAsString(),
                    transaction.getTransactionInfo().get().getId().get());
        }
        if (transaction.getTransactionInfo().get().getAggregateHash().isPresent()) {
            assertEquals(getFieldOfObject(transactionDTO, "meta", "aggregateHash").getAsString(),
                    transaction.getTransactionInfo().get().getAggregateHash().get());
        }
        if (transaction.getTransactionInfo().get().getAggregateId().isPresent()) {
            assertEquals(getFieldOfObject(transactionDTO, "meta", "aggregateId").getAsString(),
                    transaction.getTransactionInfo().get().getAggregateId().get());
        }

        assertEquals(getFieldOfObject(transactionDTO, "transaction", "signature").getAsString(), transaction.getSignature().get());
        assertEquals(getFieldOfObject(transactionDTO, "transaction", "signer").getAsString(), transaction.getSigner().get().getPublicKey());
        assertTrue(transaction.getType().getValue() == getFieldOfObject(transactionDTO, "transaction", "type").getAsInt());
        int version = TransactionMappingUtils.extractTransactionVersion(getFieldOfObject(transactionDTO, "transaction", "version"));
        assertTrue(transaction.getVersion() == version);
        int networkType = TransactionMappingUtils.extractNetworkType(getFieldOfObject(transactionDTO, "transaction", "version")).getValue();
        assertTrue(transaction.getNetworkType().getValue() == networkType);
        assertEquals(TransactionMappingUtils.extractFee(transactionDTO.get("transaction").getAsJsonObject()), transaction.getMaxFee());
        assertNotNull(transaction.getDeadline());

        if (transaction.getType() == EntityType.TRANSFER) {
            validateTransferTx((TransferTransaction) transaction, transactionDTO);
        } else if (transaction.getType() == EntityType.REGISTER_NAMESPACE) {
            validateNamespaceCreationTx((RegisterNamespaceTransaction) transaction, transactionDTO);
        } else if (transaction.getType() == EntityType.MOSAIC_DEFINITION) {
            validateMosaicCreationTx((MosaicDefinitionTransaction) transaction, transactionDTO);
        } else if (transaction.getType() == EntityType.MOSAIC_SUPPLY_CHANGE) {
            validateMosaicSupplyChangeTx((MosaicSupplyChangeTransaction) transaction, transactionDTO);
        } else if (transaction.getType() == EntityType.MODIFY_MULTISIG_ACCOUNT) {
            validateMultisigModificationTx((ModifyMultisigAccountTransaction) transaction, transactionDTO);
        } else if (transaction.getType() == EntityType.LOCK) {
            validateLockFundsTx((LockFundsTransaction) transaction, transactionDTO);
        } else if (transaction.getType() == EntityType.SECRET_LOCK) {
            validateSecretLockTx((SecretLockTransaction) transaction, transactionDTO);
        } else if (transaction.getType() == EntityType.SECRET_PROOF) {
            validateSecretProofTx((SecretProofTransaction) transaction, transactionDTO);
        }

    }

    private void validateAggregateTransaction(AggregateTransaction aggregateTransaction, JsonObject aggregateTransactionDTO) {
        assertEquals(extractBigInteger(getFieldOfObject(aggregateTransactionDTO, "meta", "height").getAsJsonArray()),
                aggregateTransaction.getTransactionInfo().get().getHeight());
        if (aggregateTransaction.getTransactionInfo().get().getHash().isPresent()) {
            assertEquals(getFieldOfObject(aggregateTransactionDTO, "meta", "hash").getAsString(),
                    aggregateTransaction.getTransactionInfo().get().getHash().get());
        }
        if (aggregateTransaction.getTransactionInfo().get().getMerkleComponentHash().isPresent()) {
            assertEquals(getFieldOfObject(aggregateTransactionDTO, "meta", "merkleComponentHash").getAsString(),
                    aggregateTransaction.getTransactionInfo().get().getMerkleComponentHash().get());
        }
        if (aggregateTransaction.getTransactionInfo().get().getIndex().isPresent()) {
            assertEquals(aggregateTransaction.getTransactionInfo().get().getIndex().get(),
                  getFieldOfObject(aggregateTransactionDTO, "meta", "index").getAsInt());
        }
        if (aggregateTransaction.getTransactionInfo().get().getId().isPresent()) {
            assertEquals(getFieldOfObject(aggregateTransactionDTO, "meta", "id").getAsString(),
                    aggregateTransaction.getTransactionInfo().get().getId().get());
        }

        assertEquals(getFieldOfObject(aggregateTransactionDTO, "transaction", "signature").getAsString(), aggregateTransaction.getSignature().get());
        assertEquals(getFieldOfObject(aggregateTransactionDTO, "transaction", "signer").getAsString(), aggregateTransaction.getSigner().get().getPublicKey());
        int version = TransactionMappingUtils.extractTransactionVersion(getFieldOfObject(aggregateTransactionDTO, "transaction", "version"));
        assertTrue(aggregateTransaction.getVersion() == version);
        int networkType = TransactionMappingUtils.extractNetworkType(getFieldOfObject(aggregateTransactionDTO, "transaction", "version")).getValue();
        assertTrue(aggregateTransaction.getNetworkType().getValue() == networkType);
        assertTrue(aggregateTransaction.getType().getValue() == getFieldOfObject(aggregateTransactionDTO, "transaction", "type").getAsInt());
        assertEquals(extractBigInteger(getFieldOfObject(aggregateTransactionDTO, "transaction", "fee").getAsJsonArray()),
                aggregateTransaction.getMaxFee());
        assertNotNull(aggregateTransaction.getDeadline());


        assertEquals(getFieldOfObject(aggregateTransactionDTO, "transaction", "cosignatures").getAsJsonArray().get(0).getAsJsonObject().get("signature").getAsString(),
                aggregateTransaction.getCosignatures().get(0).getSignature());
        assertEquals(getFieldOfObject(aggregateTransactionDTO, "transaction", "cosignatures").getAsJsonArray().get(0).getAsJsonObject().get("signer").getAsString(),
                aggregateTransaction.getCosignatures().get(0).getSigner().getPublicKey());

        Transaction innerTransaction = aggregateTransaction.getInnerTransactions().get(0);
        validateStandaloneTransaction(innerTransaction, getFieldOfObject(aggregateTransactionDTO, "transaction", "transactions").getAsJsonArray().get(0).getAsJsonObject());
    }

    private void validateTransferTx(TransferTransaction transaction, JsonObject transactionDTO) {
        assertEquals(Address.createFromEncoded(getFieldOfObject(transactionDTO, "transaction", "recipient").getAsString()), transaction.getRecipient().getAddress().orElseThrow(RuntimeException::new));

        JsonArray mosaicsDTO = getFieldOfObject(transactionDTO, "transaction", "mosaics").getAsJsonArray();
        if (mosaicsDTO != null && mosaicsDTO.size() > 0) {
            assertEquals(extractBigInteger(mosaicsDTO.get(0).getAsJsonObject().get("id").getAsJsonArray()),
                    transaction.getMosaics().get(0).getId().getId());
            assertEquals(extractBigInteger(mosaicsDTO.get(0).getAsJsonObject().get("amount").getAsJsonArray()),
                    transaction.getMosaics().get(0).getAmount());
        }

		if (getFieldOfObject(transactionDTO, "transaction", "message") != null) {
			try {
				assertEquals(
						new String(Hex.decode(getFieldOfObject(transactionDTO, "transaction", "message").getAsJsonObject()
								.get("payload").getAsString()), "UTF-8"),
						new String(transaction.getMessage().getEncodedPayload(), "UTF-8"));
		        assertTrue(getFieldOfObject(transactionDTO, "transaction", "message").getAsJsonObject().get("type").getAsInt() ==
		                transaction.getMessage().getTypeCode());
			} catch (UnsupportedEncodingException e) {
				fail("unsupported encoing", e);
			}
		} else {
			assertEquals("", transaction.getMessage().getPayload());
		}
    }

    private void validateNamespaceCreationTx(RegisterNamespaceTransaction transaction, JsonObject transactionDTO) {
        assertTrue(getFieldOfObject(transactionDTO, "transaction", "namespaceType").getAsInt() ==
                transaction.getNamespaceType().getValue());
        assertEquals(getFieldOfObject(transactionDTO, "transaction", "name").getAsString(),
                transaction.getNamespaceName());
        assertEquals(extractBigInteger(getFieldOfObject(transactionDTO, "transaction", "namespaceId").getAsJsonArray()),
                transaction.getNamespaceId().getId());

        if (transaction.getNamespaceType() == NamespaceType.ROOT_NAMESPACE) {
            assertEquals(extractBigInteger(getFieldOfObject(transactionDTO, "transaction", "duration").getAsJsonArray()),
                    transaction.getDuration().get());
        } else {
            assertEquals(extractBigInteger(getFieldOfObject(transactionDTO, "transaction", "parentId").getAsJsonArray()),
                    transaction.getParentId().get().getId());
        }
    }

    private void validateMosaicCreationTx(MosaicDefinitionTransaction transaction, JsonObject transactionDTO) {
        assertEquals(extractBigInteger(getFieldOfObject(transactionDTO, "transaction", "mosaicId").getAsJsonArray()),
                transaction.getMosaicId().getId());
        JsonArray properties = getFieldOfObject(transactionDTO, "transaction", "properties").getAsJsonArray();
        assertEquals(transaction.getMosaicProperties().getDivisibility(), properties.get(1).getAsJsonObject().get("value").getAsJsonArray().get(0).getAsInt());
        if (properties.size() > 2) {
        	assertEquals(extractBigInteger(properties.get(2).getAsJsonObject().get("value").getAsJsonArray()), transaction.getMosaicProperties().getDuration().orElse(BigInteger.valueOf(-98765)));
        } else {
        	assertFalse(transaction.getMosaicProperties().getDuration().isPresent());
        }
    }

    private void validateMosaicSupplyChangeTx(MosaicSupplyChangeTransaction transaction, JsonObject transactionDTO) {
        assertEquals(extractBigInteger(getFieldOfObject(transactionDTO, "transaction", "mosaicId").getAsJsonArray()),
                transaction.getMosaicId().getId());
        assertEquals(extractBigInteger(getFieldOfObject(transactionDTO, "transaction", "delta").getAsJsonArray()),
                transaction.getDelta());
        assertTrue(transaction.getMosaicSupplyType().getValue() == getFieldOfObject(transactionDTO, "transaction", "direction").getAsInt());
    }

    private void validateMultisigModificationTx(ModifyMultisigAccountTransaction transaction, JsonObject transactionDTO) {
        assertTrue(transaction.getMinApprovalDelta() == getFieldOfObject(transactionDTO, "transaction", "minApprovalDelta").getAsInt());
        assertTrue(transaction.getMinRemovalDelta() == getFieldOfObject(transactionDTO, "transaction", "minRemovalDelta").getAsInt());
        assertEquals(getFieldOfObject(transactionDTO, "transaction", "modifications").getAsJsonArray().get(0).getAsJsonObject().get("cosignatoryPublicKey").getAsString(),
                transaction.getModifications().get(0).getCosignatoryPublicAccount().getPublicKey());
        assertTrue(getFieldOfObject(transactionDTO, "transaction", "modifications").getAsJsonArray().get(0).getAsJsonObject().get("type").getAsInt() ==
                transaction.getModifications().get(0).getType().getValue());
    }

    private void validateLockFundsTx(LockFundsTransaction transaction, JsonObject transactionDTO) {
        assertEquals(extractBigInteger(getFieldOfObject(transactionDTO, "transaction", "mosaicId").getAsJsonArray()),
                transaction.getMosaic().getId().getId());
        assertEquals(extractBigInteger(getFieldOfObject(transactionDTO, "transaction", "amount").getAsJsonArray()),
                transaction.getMosaic().getAmount());
        assertEquals(extractBigInteger(getFieldOfObject(transactionDTO, "transaction", "duration").getAsJsonArray()),
                transaction.getDuration());
        assertEquals(getFieldOfObject(transactionDTO, "transaction", "hash").getAsString(),
                transaction.getSignedTransaction().getHash());
    }

    private void validateSecretLockTx(SecretLockTransaction transaction, JsonObject transactionDTO) {
        assertEquals(extractBigInteger(getFieldOfObject(transactionDTO, "transaction", "mosaicId").getAsJsonArray()),
                transaction.getMosaic().getId().getId());
        assertEquals(extractBigInteger(getFieldOfObject(transactionDTO, "transaction", "amount").getAsJsonArray()),
                transaction.getMosaic().getAmount());
        assertEquals(extractBigInteger(getFieldOfObject(transactionDTO, "transaction", "duration").getAsJsonArray()),
                transaction.getDuration());
        assertTrue(getFieldOfObject(transactionDTO, "transaction", "hashAlgorithm").getAsInt() ==
                transaction.getHashType().getValue());
        assertEquals(getFieldOfObject(transactionDTO, "transaction", "secret").getAsString(),
                transaction.getSecret());
        assertEquals(Address.createFromEncoded(getFieldOfObject(transactionDTO, "transaction", "recipient").getAsString()), transaction.getRecipient());
    }

    private void validateSecretProofTx(SecretProofTransaction transaction, JsonObject transactionDTO) {
        assertTrue(getFieldOfObject(transactionDTO, "transaction", "hashAlgorithm").getAsInt() ==
                transaction.getHashType().getValue());
        assertEquals(getFieldOfObject(transactionDTO, "transaction", "secret").getAsString(),
                transaction.getSecret());
        assertEquals(getFieldOfObject(transactionDTO, "transaction", "proof").getAsString(),
                transaction.getProof());
    }

    private BigInteger extractBigInteger(JsonArray input) {
        ArrayList<Integer> uInt64DTO = new ArrayList<>();
        GsonUtils.stream(input).forEach(item -> uInt64DTO.add(item.getAsInt()));
        return UInt64Utils.toBigInt(uInt64DTO);
    }
}
