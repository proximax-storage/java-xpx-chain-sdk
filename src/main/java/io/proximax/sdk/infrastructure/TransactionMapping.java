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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.spongycastle.util.encoders.Hex;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.account.PublicAccount;
import io.proximax.sdk.model.account.props.AccountPropertyModification;
import io.proximax.sdk.model.account.props.AccountPropertyModificationType;
import io.proximax.sdk.model.account.props.AccountPropertyType;
import io.proximax.sdk.model.alias.AliasAction;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.metadata.MetadataModification;
import io.proximax.sdk.model.metadata.MetadataModificationType;
import io.proximax.sdk.model.metadata.MetadataType;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.mosaic.MosaicSupplyType;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceType;
import io.proximax.sdk.model.transaction.*;
import io.proximax.sdk.utils.GsonUtils;
import io.reactivex.functions.Function;

public class TransactionMapping implements Function<JsonObject, Transaction> {
    @Override
   public Transaction apply(JsonObject input) {
      JsonObject transaction = input.getAsJsonObject("transaction");
      int type = transaction.get("type").getAsInt();

      switch (TransactionType.rawValueOf(type)) {
      case TRANSFER:
         return new TransferTransactionMapping().apply(input);
      case REGISTER_NAMESPACE:
         return new NamespaceCreationTransactionMapping().apply(input);
      case MOSAIC_DEFINITION:
         return new MosaicCreationTransactionMapping().apply(input);
      case MOSAIC_SUPPLY_CHANGE:
         return new MosaicSupplyChangeTransactionMapping().apply(input);
      case MOSAIC_ALIAS:
         return new MosaicAliasTransactionMapping().apply(input);
      case ADDRESS_ALIAS:
         return new AddressAliasTransactionMapping().apply(input);
      case MODIFY_MULTISIG_ACCOUNT:
         return new MultisigModificationTransactionMapping().apply(input);
      case AGGREGATE_COMPLETE:
      case AGGREGATE_BONDED:
         return new AggregateTransactionMapping().apply(input);
      case LOCK:
         return new LockFundsTransactionMapping().apply(input);
      case SECRET_LOCK:
         return new SecretLockTransactionMapping().apply(input);
      case SECRET_PROOF:
         return new SecretProofTransactionMapping().apply(input);
      case MODIFY_ADDRESS_METADATA:
      case MODIFY_MOSAIC_METADATA:
      case MODIFY_NAMESPACE_METADATA:
         return new ModifyMetadataTransactionMapping().apply(input);
      case MODIFY_CONTRACT:
         return new ModifyContractTransactionMapping().apply(input);         
      case ACCOUNT_PROPERTIES_ADDRESS: case ACCOUNT_PROPERTIES_MOSAIC: case ACCOUNT_PROPERTIES_ENTITY_TYPE:
         return new ModifyAccountPropertiesTransactionMapping().apply(input);
      default:
         throw new UnsupportedOperationException("Unimplemented transaction type " + type);
      }
   }

    /**
     * take array of unsigned integers and combine them to BigInteger
     * 
     * @param input JSON array
     * @return big integer represented by the array
     */
    public static BigInteger extractBigInteger(JsonArray input) {
        return GsonUtils.getBigInteger(input);
    }

    /**
     * extract transaction version from version
     * 
     * @param version version field of transaction
     * @return transaction version
     */
    Integer extractTransactionVersion(JsonElement elem) {
       int version = elem.getAsInt();
    	// take second most significant byte of a version and return it as a number
        return (int) Long.parseLong(Integer.toHexString(version).substring(2, 4), 16);
    }

    /**
     * extract network type from the version
     * 
     * @param version version field of transaction
     * @return transaction network type
     */
    NetworkType extractNetworkType(JsonElement elem) {
       int version = elem.getAsInt();
    	// take most significant byte of a version and return it as a number
        int networkType = (int) Long.parseLong(Integer.toHexString(version).substring(0, 2), 16);
        return NetworkType.rawValueOf(networkType);
    }

    /**
     * retrieve fee from the transaction. listener returns fee as uint64 "fee" and services return string "maxFee"
     * and this method provides support for both
     * 
     * @param transaction transaction object with fee or maxFee field
     * @return value of the fee
     */
   public static BigInteger extractFee(JsonObject transaction) {
      // first get value from a field
      JsonElement feeElement = transaction.get("maxFee");
      if (feeElement == null) {
         feeElement = transaction.get("fee");
      }
      // based on the retrieved value create big integer instance
      if (feeElement instanceof JsonArray) {
         return extractBigInteger((JsonArray) feeElement);
      } else {
         return new BigInteger(feeElement.getAsString());
      }
   }
   
    /**
     * create transaction info based on the provided transaction meta json object
     * 
     * @param jsonObject json object representing the meta field
     * @return TransactionInfo instance representing the meta
     */
    public TransactionInfo createTransactionInfo(JsonObject jsonObject) {
    	// hash and id indicate standard transaction info
        if (jsonObject.has("hash") && jsonObject.has("id")) {
            return TransactionInfo.create(
            		extractBigInteger(jsonObject.getAsJsonArray("height")),
                    jsonObject.get("index").getAsInt(),
                    jsonObject.get("id").getAsString(),
                    jsonObject.get("hash").getAsString(),
                    jsonObject.get("merkleComponentHash").getAsString());
        }
    	// agrregateHash and id indicate aggregate transaction
        if (jsonObject.has("aggregateHash") && jsonObject.has("id")) {
            return TransactionInfo.createAggregate(extractBigInteger(jsonObject.getAsJsonArray("height")),
                    jsonObject.get("index").getAsInt(),
                    jsonObject.get("id").getAsString(),
                    jsonObject.get("aggregateHash").getAsString(),
                    jsonObject.get("aggregateId").getAsString());
        }
        // transaction with missing id
        return TransactionInfo.create(extractBigInteger(jsonObject.getAsJsonArray("height")),
        		jsonObject.get("hash").getAsString(),
                jsonObject.get("merkleComponentHash").getAsString());
    }
    
    /**
     * convert JSON array to stream of JSON elements
     * 
     * @param jsonArr array with elements
     * @return stream of elements
     */
    static Stream<JsonElement> stream(JsonArray jsonArr) {
       return StreamSupport.stream(jsonArr.spliterator(), false);
    }
}

/**
 * mapping for transfer transaction
 */
class TransferTransactionMapping extends TransactionMapping {

    @Override
    public TransferTransaction apply(JsonObject input) {
    	// retrieve transaction info from meta field
        TransactionInfo transactionInfo = createTransactionInfo(input.getAsJsonObject("meta"));
        // retrieve transaction data from transaction field
        JsonObject transaction = input.getAsJsonObject("transaction");
        // deadline
        DeadlineBP deadline = new DeadlineBP(extractBigInteger(transaction.getAsJsonArray("deadline")));
        // mosaics
        List<Mosaic> mosaics;
        if (transaction.getAsJsonArray("mosaics") != null) {
            mosaics = stream(transaction.getAsJsonArray("mosaics"))
                    .map(item -> (JsonObject) item)
                    .map(mosaic -> new Mosaic(
                            new MosaicId(extractBigInteger(mosaic.getAsJsonArray("id"))),
                            extractBigInteger(mosaic.getAsJsonArray("amount"))))
                    .collect(Collectors.toList());
        } else {
        	mosaics = new ArrayList<>();
        }
        // message
        Message message;
        if (transaction.getAsJsonObject("message") != null) {
            final JsonObject messageObj = transaction.getAsJsonObject("message");
            int messageType = messageObj.get("type").getAsInt();
            String messagePayload = messageObj.get("payload").getAsString();
            message = MessageFactory.createMessage(messageType, Hex.decode(messagePayload));
        } else {
        	message = PlainMessage.Empty;
        }

        // version
        JsonElement version = transaction.get("version");
        // create transfer transaction instance
        return new TransferTransaction(
                extractNetworkType(version),
                extractTransactionVersion(version),
                deadline,
                extractFee(transaction),
                Recipient.from(Address.createFromEncoded(transaction.get("recipient").getAsString())),
                mosaics,
                message,
                transaction.get("signature").getAsString(),
                new PublicAccount(transaction.get("signer").getAsString(), extractNetworkType(version)),
                transactionInfo
        );
    }
}

/**
 * mapping for metadata transaction
 */
class ModifyMetadataTransactionMapping extends TransactionMapping {

   @Override
   public ModifyMetadataTransaction apply(JsonObject input) {
      // retrieve transaction info from meta field
      TransactionInfo transactionInfo = createTransactionInfo(input.getAsJsonObject("meta"));
      // retrieve transaction data from transaction field
      JsonObject transaction = input.getAsJsonObject("transaction");
      // deadline
      DeadlineBP deadline = new DeadlineBP(extractBigInteger(transaction.getAsJsonArray("deadline")));
      // version
      JsonElement version = transaction.get("version");
      // transaction type
      TransactionType type = TransactionType.rawValueOf(transaction.get("type").getAsInt());
      // modifications
      List<MetadataModification> modifications = stream(transaction.getAsJsonArray("modifications"))
            .map(obj -> (JsonObject) obj).map(ModifyMetadataTransactionMapping::mapModFromJson)
            .collect(Collectors.toList());
      // signer
      PublicAccount signer = new PublicAccount(transaction.get("signer").getAsString(), extractNetworkType(version));
      // signature
      String signature = transaction.get("signature").getAsString();
      // metadata type
      MetadataType metadataType = MetadataType.getByCode(transaction.get("metadataType").getAsInt());
      // create instance of transaction
      switch (type) {
      case MODIFY_ADDRESS_METADATA:
         return new ModifyMetadataTransaction(type, extractNetworkType(version), extractTransactionVersion(version),
               deadline, extractFee(transaction), Optional.empty(),
               Optional.of(Address.createFromEncoded(transaction.get("metadataId").getAsString())), metadataType, modifications,
               signature, signer, transactionInfo);
      case MODIFY_MOSAIC_METADATA:
         return new ModifyMetadataTransaction(type, extractNetworkType(version), extractTransactionVersion(version),
               deadline, extractFee(transaction),
               Optional.of(new MosaicId(extractBigInteger(transaction.getAsJsonArray("metadataId")))), Optional.empty(),
               metadataType, modifications, signature, signer, transactionInfo);
      case MODIFY_NAMESPACE_METADATA:
         return new ModifyMetadataTransaction(type, extractNetworkType(version), extractTransactionVersion(version),
               deadline, extractFee(transaction),
               Optional.of(new NamespaceId(extractBigInteger(transaction.getAsJsonArray("metadataId")))),
               Optional.empty(), metadataType, modifications, signature, signer, transactionInfo);
      default:
         throw new IllegalArgumentException("unsupported transaction type " + type);
      }
   }

   /**
    * transform JSON object into metadata modification instance
    * 
    * @param json JSON representation of the modification
    * @return metadata modification
    */
   static MetadataModification mapModFromJson(JsonObject json) {
      MetadataModificationType type = MetadataModificationType.getByCode(json.get("modificationType").getAsInt());
      String key = json.get("key").getAsString();
      switch (type) {
      case ADD:
         return MetadataModification.add(key, json.get("value").getAsString());
      case REMOVE:
         return MetadataModification.remove(key);
      default:
         throw new IllegalArgumentException("modification type not supported: " + type);
      }
   }
}

/**
 * mapping for metadata transaction
 */
class ModifyAccountPropertiesTransactionMapping extends TransactionMapping {

   @Override
   public ModifyAccountPropertyTransaction<?> apply(JsonObject input) {
      // retrieve transaction info from meta field
      TransactionInfo transactionInfo = createTransactionInfo(input.getAsJsonObject("meta"));
      // retrieve transaction data from transaction field
      JsonObject transaction = input.getAsJsonObject("transaction");
      // deadline
      DeadlineBP deadline = new DeadlineBP(extractBigInteger(transaction.getAsJsonArray("deadline")));
      // version
      JsonElement version = transaction.get("version");
      // transaction type
      TransactionType type = TransactionType.rawValueOf(transaction.get("type").getAsInt());
      // signer
      PublicAccount signer = new PublicAccount(transaction.get("signer").getAsString(), extractNetworkType(version));
      // signature
      String signature = transaction.get("signature").getAsString();
      // metadata type
      AccountPropertyType propertyType = AccountPropertyType.getByCode(transaction.get("propertyType").getAsInt());
      // create instance of transaction
      switch (type) {
      case ACCOUNT_PROPERTIES_ADDRESS:
         return new ModifyAccountPropertyTransaction.AddressModification(
               extractNetworkType(version), 
               extractTransactionVersion(version),
               deadline, 
               extractFee(transaction), 
               propertyType, 
               getAddressMods(transaction),
               Optional.of(signature), 
               Optional.of(signer), 
               Optional.of(transactionInfo));
      case ACCOUNT_PROPERTIES_MOSAIC:
         return new ModifyAccountPropertyTransaction.MosaicModification(
               extractNetworkType(version), 
               extractTransactionVersion(version),
               deadline, 
               extractFee(transaction), 
               propertyType, 
               getMosaicMods(transaction),
               Optional.of(signature), 
               Optional.of(signer), 
               Optional.of(transactionInfo));
      case ACCOUNT_PROPERTIES_ENTITY_TYPE:
         return new ModifyAccountPropertyTransaction.EntityTypeModification(
               extractNetworkType(version), 
               extractTransactionVersion(version),
               deadline, 
               extractFee(transaction), 
               propertyType, 
               getEntityTypeMods(transaction),
               Optional.of(signature), 
               Optional.of(signer), 
               Optional.of(transactionInfo));
      default:
         throw new IllegalArgumentException("unsupported transaction type " + type);
      }
   }
   
   /**
    * create list of address account property modifications
    * 
    * @param transaction the transaction object to get data from
    * @return the list of address account property modifications
    */
   static List<AccountPropertyModification<Address>> getAddressMods(JsonObject transaction) {
      return stream(transaction.getAsJsonArray("modifications")).map(obj -> (JsonObject) obj).map(json -> {
         AccountPropertyModificationType modType = AccountPropertyModificationType
               .getByCode(json.get("modificationType").getAsInt());
         return new AccountPropertyModification<>(modType, Address.createFromEncoded(json.get("value").getAsString()));
      }).collect(Collectors.toList());
   }
   
   /**
    * create list of mosaic account property modifications
    * 
    * @param transaction the transaction object to get data from
    * @return the list of mosaic account property modifications
    */
   static List<AccountPropertyModification<MosaicId>> getMosaicMods(JsonObject transaction) {
      return stream(transaction.getAsJsonArray("modifications")).map(obj -> (JsonObject) obj).map(json -> {
         AccountPropertyModificationType modType = AccountPropertyModificationType
               .getByCode(json.get("modificationType").getAsInt());
         return new AccountPropertyModification<>(modType,
               new MosaicId(extractBigInteger(json.get("value").getAsJsonArray())));
      }).collect(Collectors.toList());
   }
   
   /**
    * create list of entity type account property modifications
    * 
    * @param transaction the transaction object to get data from
    * @return the list of entity type account property modifications
    */
   static List<AccountPropertyModification<TransactionType>> getEntityTypeMods(JsonObject transaction) {
      return stream(transaction.getAsJsonArray("modifications")).map(obj -> (JsonObject) obj).map(json -> {
         AccountPropertyModificationType modType = AccountPropertyModificationType
               .getByCode(json.get("modificationType").getAsInt());
         return new AccountPropertyModification<>(modType,
               TransactionType.rawValueOf(json.get("value").getAsInt()));
      }).collect(Collectors.toList());
   }
}

/**
 * mapper for transaction that registers namespace
 */
class NamespaceCreationTransactionMapping extends TransactionMapping {

    @Override
    public RegisterNamespaceTransaction apply(JsonObject input) {
    	// get transaction info and data
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));
        JsonObject transaction = input.getAsJsonObject("transaction");
        // retrieve fields
        DeadlineBP deadline = new DeadlineBP(extractBigInteger(transaction.getAsJsonArray("deadline")));
        NamespaceType namespaceType = NamespaceType.rawValueOf(transaction.get("namespaceType").getAsInt());
        NamespaceId namespaceId = new NamespaceId(extractBigInteger(transaction.getAsJsonArray("namespaceId")));
        Optional<BigInteger> namespaceDuration = namespaceType == NamespaceType.RootNamespace ? Optional.of(extractBigInteger(transaction.getAsJsonArray("duration"))) : Optional.empty();
        Optional<NamespaceId> namespaceParentId = namespaceType == NamespaceType.SubNamespace ? Optional.of(new NamespaceId(extractBigInteger(transaction.getAsJsonArray("parentId")))) : Optional.empty();
        JsonElement version = transaction.get("version");
        // return the register namespace transaction
        return new RegisterNamespaceTransaction(
                extractNetworkType(version),
                extractTransactionVersion(version),
                deadline,
                extractFee(transaction),
                transaction.get("name").getAsString(),
                namespaceId,
                namespaceType,
                namespaceDuration,
                namespaceParentId,
                transaction.get("signature").getAsString(),
                new PublicAccount(transaction.get("signer").getAsString(), extractNetworkType(version)),
                transactionInfo
        );
    }
}

/**
 * transaction that creates new mosaic
 */
class MosaicCreationTransactionMapping extends TransactionMapping {

    @Override
    public MosaicDefinitionTransaction apply(JsonObject input) {
    	// load transaction info and data
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));
        JsonObject transaction = input.getAsJsonObject("transaction");
        // load data fields
        DeadlineBP deadline = new DeadlineBP(extractBigInteger(transaction.getAsJsonArray("deadline")));
        // construct properties
        JsonArray mosaicProperties = transaction.getAsJsonArray("properties");
        String flags = "00" + Integer.toBinaryString(extractBigInteger(mosaicProperties.get(0).getAsJsonObject().getAsJsonArray("value")).intValue());
        String bitMapFlags = flags.substring(flags.length() - 3, flags.length());
        MosaicProperties properties = new MosaicProperties(bitMapFlags.charAt(2) == '1',
                bitMapFlags.charAt(1) == '1',
                bitMapFlags.charAt(0) == '1',
                extractBigInteger(mosaicProperties.get(1).getAsJsonObject().getAsJsonArray("value")).intValue(),
                mosaicProperties.size() == 3 ? extractBigInteger(mosaicProperties.get(2).getAsJsonObject().getAsJsonArray("value")) : BigInteger.valueOf(0));
        JsonElement version = transaction.get("version");
        // return instance of mosaic definition transaction
        return new MosaicDefinitionTransaction(
                extractNetworkType(version),
                extractTransactionVersion(version),
                deadline,
                extractFee(transaction),
                extractNonce(transaction),
                new MosaicId(extractBigInteger(transaction.getAsJsonArray("mosaicId"))),
                properties,
                transaction.get("signature").getAsString(),
                new PublicAccount(transaction.get("signer").getAsString(), extractNetworkType(version)),
                transactionInfo
        );
    }
    
    /**
     * retrieve fee from the transaction. listener returns fee as uint64 "fee" and services return string "maxFee"
     * and this method provides support for both
     * 
     * @param transaction transaction object with fee or maxFee field
     * @return value of the fee
     */
    public static MosaicNonce extractNonce(JsonObject transaction) {
      if (transaction.has("mosaicNonce")) {
         return MosaicNonce.createFromBigInteger(transaction.get("mosaicNonce").getAsBigInteger());
      } else if (transaction.has("nonce")) {
         return MosaicNonce.createFromBigInteger(transaction.get("nonce").getAsBigInteger());
      } else {
         throw new IllegalArgumentException("Fee is missing in the transaction description");
      }
   }
}

/**
 * transaction that creates alias between namespace and mosaic
 */
class MosaicAliasTransactionMapping extends TransactionMapping {

    @Override
    public AliasTransaction apply(JsonObject input) {
      // load transaction info and data
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));
        JsonObject transaction = input.getAsJsonObject("transaction");
        // load data fields
        DeadlineBP deadline = new DeadlineBP(extractBigInteger(transaction.getAsJsonArray("deadline")));
        JsonElement version = transaction.get("version");
        // return instance of mosaic alias definition transaction
        return new AliasTransaction(
                TransactionType.MOSAIC_ALIAS,
                extractNetworkType(version),
                extractTransactionVersion(version),
                deadline,
                extractFee(transaction),
                Optional.of(new MosaicId(extractBigInteger(transaction.getAsJsonArray("mosaicId")))),
                Optional.empty(),
                new NamespaceId(extractBigInteger(transaction.getAsJsonArray("namespaceId"))),
                extractAliasAction(transaction),
                transaction.get("signature").getAsString(),
                new PublicAccount(transaction.get("signer").getAsString(), extractNetworkType(version)),
                transactionInfo
        );
    }
    
   private static AliasAction extractAliasAction(JsonObject json) {
      Integer aliasCode;
      // try aliasAction field
      if (json.has("aliasAction")) {
         aliasCode = json.get("aliasAction").getAsInt();
      } else {
         // try alias
         aliasCode = json.get("action").getAsInt();
      }
      return AliasAction.getBycode(aliasCode);
   }
}

/**
 * transaction that creates alias between namespace and address
 */
class AddressAliasTransactionMapping extends TransactionMapping {

    @Override
    public AliasTransaction apply(JsonObject input) {
      // load transaction info and data
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));
        JsonObject transaction = input.getAsJsonObject("transaction");
        // load data fields
        DeadlineBP deadline = new DeadlineBP(extractBigInteger(transaction.getAsJsonArray("deadline")));
        JsonElement version = transaction.get("version");
        // return instance of mosaic alias definition transaction
        return new AliasTransaction(
                TransactionType.ADDRESS_ALIAS,
                extractNetworkType(version),
                extractTransactionVersion(version),
                deadline,
                extractFee(transaction),
                Optional.empty(),
                Optional.of(Address.createFromEncoded(transaction.get("address").getAsString())),
                new NamespaceId(extractBigInteger(transaction.getAsJsonArray("namespaceId"))),
                AliasAction.getBycode(transaction.get("aliasAction").getAsInt()),
                transaction.get("signature").getAsString(),
                new PublicAccount(transaction.get("signer").getAsString(), extractNetworkType(version)),
                transactionInfo
        );
    }
}

class MosaicSupplyChangeTransactionMapping extends TransactionMapping {

    @Override
    public MosaicSupplyChangeTransaction apply(JsonObject input) {
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

        JsonObject transaction = input.getAsJsonObject("transaction");
        DeadlineBP deadline = new DeadlineBP(extractBigInteger(transaction.getAsJsonArray("deadline")));

        return new MosaicSupplyChangeTransaction(
                extractNetworkType(transaction.get("version")),
                extractTransactionVersion(transaction.get("version")),
                deadline,
                extractFee(transaction),
                new MosaicId(extractBigInteger(transaction.getAsJsonArray("mosaicId"))),
                MosaicSupplyType.rawValueOf(transaction.get("direction").getAsInt()),
                extractBigInteger(transaction.getAsJsonArray("delta")),
                transaction.get("signature").getAsString(),
                new PublicAccount(transaction.get("signer").getAsString(), extractNetworkType(transaction.get("version"))),
                transactionInfo
        );
    }
}

class MultisigModificationTransactionMapping extends TransactionMapping {

   @Override
   public ModifyMultisigAccountTransaction apply(JsonObject input) {
       TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

       JsonObject transaction = input.getAsJsonObject("transaction");
       DeadlineBP deadline = new DeadlineBP(extractBigInteger(transaction.getAsJsonArray("deadline")));
       NetworkType networkType = extractNetworkType(transaction.get("version"));

       List<MultisigCosignatoryModification> modifications = transaction.has("modifications") ? stream(transaction.getAsJsonArray("modifications"))
               .map(item -> (JsonObject) item)
               .map(multisigModification -> new MultisigCosignatoryModification(
                       MultisigCosignatoryModificationType.rawValueOf(multisigModification.get("type").getAsInt()),
                       PublicAccount.createFromPublicKey(multisigModification.get("cosignatoryPublicKey").getAsString(), networkType)))
               .collect(Collectors.toList()) : Collections.emptyList();

       return new ModifyMultisigAccountTransaction(
               networkType,
               extractTransactionVersion(transaction.get("version")),
               deadline,
               extractFee(transaction),
               transaction.get("minApprovalDelta").getAsInt(),
               transaction.get("minRemovalDelta").getAsInt(),
               modifications,
               transaction.get("signature").getAsString(),
               new PublicAccount(transaction.get("signer").getAsString(), networkType),
               transactionInfo
       );
   }
}

/**
 * Modify contract transaction mapping from JSON to Transaction instance
 */
class ModifyContractTransactionMapping extends TransactionMapping {

   @Override
   public ModifyContractTransaction apply(JsonObject input) {
       TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

       JsonObject transaction = input.getAsJsonObject("transaction");
       DeadlineBP deadline = new DeadlineBP(extractBigInteger(transaction.getAsJsonArray("deadline")));
       NetworkType networkType = extractNetworkType(transaction.get("version"));

       return new ModifyContractTransaction(
               networkType,
               extractTransactionVersion(transaction.get("version")),
               deadline,
               extractFee(transaction),
               Optional.of(transaction.get("signature").getAsString()),
               Optional.of(new PublicAccount(transaction.get("signer").getAsString(), networkType)),
               Optional.of(transactionInfo),
               extractBigInteger(transaction.get("duration").getAsJsonArray()),
               transaction.get("hash").getAsString(),
               extractModifications(networkType, transaction, "customers"),
               extractModifications(networkType, transaction, "executors"),
               extractModifications(networkType, transaction, "verifiers")
       );
   }

   /**
    * extract multisig modifications from the transaction and specified field name
    * 
    * @param networkType network type
    * @param transaction transaction JSON object
    * @param fieldName name of the field to get the modifications from
    * @return the list of multisig modifications
    */
   private List<MultisigCosignatoryModification> extractModifications(NetworkType networkType, JsonObject transaction,
         String fieldName) {
      // if there is no such field then return empty list
      if (!transaction.has(fieldName)) {
         return Collections.emptyList();
      }
      // load items from the field
      return stream(transaction.getAsJsonArray(fieldName)).map(item -> (JsonObject) item)
            .map(multisigModification -> new MultisigCosignatoryModification(
                  MultisigCosignatoryModificationType.rawValueOf(multisigModification.get("type").getAsInt()),
                  PublicAccount.createFromPublicKey(multisigModification.get("cosignatoryPublicKey").getAsString(),
                        networkType)))
            .collect(Collectors.toList());
   }
}

class AggregateTransactionMapping extends TransactionMapping {

    @Override
    public AggregateTransaction apply(JsonObject input) {
       
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

        JsonObject transaction = input.getAsJsonObject("transaction");
        DeadlineBP deadline = new DeadlineBP(extractBigInteger(transaction.getAsJsonArray("deadline")));
        NetworkType networkType = extractNetworkType(transaction.get("version"));

        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < transaction.getAsJsonArray("transactions").size(); i++) {
            JsonObject innerTransaction = transaction.getAsJsonArray("transactions").get(i).getAsJsonObject();
            innerTransaction.getAsJsonObject("transaction").add("deadline", transaction.getAsJsonArray("deadline"));
            setFee(innerTransaction.getAsJsonObject("transaction"), transaction);
            innerTransaction.getAsJsonObject("transaction").add("fee", transaction.getAsJsonArray("fee"));
            innerTransaction.getAsJsonObject("transaction").add("signature", transaction.get("signature"));
            if (!innerTransaction.has("meta")) {
                innerTransaction.add("meta", input.getAsJsonObject("meta"));
            }
            transactions.add(new TransactionMapping().apply(innerTransaction));
        }

        List<AggregateTransactionCosignature> cosignatures = new ArrayList<>();
        if (transaction.getAsJsonArray("cosignatures") != null) {
            cosignatures = stream(transaction
                    .getAsJsonArray("cosignatures"))
                    .map(item -> (JsonObject) item)
                    .map(aggregateCosignature -> new AggregateTransactionCosignature(
                            aggregateCosignature.get("signature").getAsString(),
                            new PublicAccount(aggregateCosignature.get("signer").getAsString(), networkType)))
                    .collect(Collectors.toList());
        }

        return new AggregateTransaction(
                networkType,
                TransactionType.rawValueOf(transaction.get("type").getAsInt()),
                extractTransactionVersion(transaction.get("version")),
                deadline,
                extractFee(transaction),
                transactions,
                cosignatures,
                transaction.get("signature").getAsString(),
                new PublicAccount(transaction.get("signer").getAsString(), networkType),
                transactionInfo
        );
    }
    
    /**
     * copy the fee field from the source to target object
     * 
     * @param target JSON object to copy the fee to
     * @param source JSON object to take the fee from
     */
    private void setFee(JsonObject target, JsonObject source) {
       if (source.has("maxFee")) {
          target.add("maxFee", source.get("maxFee"));
       } else if (source.has("fee")) {
          target.add("fee", source.get("fee"));
       } else {
          throw new IllegalArgumentException("Fee is missing in the transaction description");
       }
    }
}

class LockFundsTransactionMapping extends TransactionMapping {

    @Override
    public LockFundsTransaction apply(JsonObject input) {
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

        JsonObject transaction = input.getAsJsonObject("transaction");
        DeadlineBP deadline = new DeadlineBP(extractBigInteger(transaction.getAsJsonArray("deadline")));
        NetworkType networkType = extractNetworkType(transaction.get("version"));
        Mosaic mosaic;
        if (transaction.has("mosaicId")) {
            mosaic = new Mosaic(new MosaicId(extractBigInteger(transaction.getAsJsonArray("mosaicId"))), extractBigInteger(transaction.getAsJsonArray("amount")));
        } else {
            mosaic = new Mosaic(new MosaicId(extractBigInteger(transaction.getAsJsonObject("mosaic").getAsJsonArray("id"))), extractBigInteger(transaction.getAsJsonObject("mosaic").getAsJsonArray("amount")));
        }
        return new LockFundsTransaction(
                networkType,
                extractTransactionVersion(transaction.get("version")),
                deadline,
                extractFee(transaction),
                mosaic,
                extractBigInteger(transaction.getAsJsonArray("duration")),
                new SignedTransaction("", transaction.get("hash").getAsString(), TransactionType.AGGREGATE_BONDED),
                transaction.get("signature").getAsString(),
                new PublicAccount(transaction.get("signer").getAsString(), networkType),
                transactionInfo
        );
    }
}

class SecretLockTransactionMapping extends TransactionMapping {

    @Override
    public SecretLockTransaction apply(JsonObject input) {
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

        JsonObject transaction = input.getAsJsonObject("transaction");
        DeadlineBP deadline = new DeadlineBP(extractBigInteger(transaction.getAsJsonArray("deadline")));
        NetworkType networkType = extractNetworkType(transaction.get("version"));
        Mosaic mosaic;
        if (transaction.has("mosaicId")) {
            mosaic = new Mosaic(new MosaicId(extractBigInteger(transaction.getAsJsonArray("mosaicId"))), extractBigInteger(transaction.getAsJsonArray("amount")));
        } else {
            mosaic = new Mosaic(new MosaicId(extractBigInteger(transaction.getAsJsonObject("mosaic").getAsJsonArray("id"))), extractBigInteger(transaction.getAsJsonObject("mosaic").getAsJsonArray("amount")));
        }
        return new SecretLockTransaction(
                networkType,
                extractTransactionVersion(transaction.get("version")),
                deadline,
                extractFee(transaction),
                mosaic,
                extractBigInteger(transaction.getAsJsonArray("duration")),
                HashType.rawValueOf(transaction.get("hashAlgorithm").getAsInt()),
                transaction.get("secret").getAsString(),
                Address.createFromEncoded(transaction.get("recipient").getAsString()),
                transaction.get("signature").getAsString(),
                new PublicAccount(transaction.get("signer").getAsString(), networkType),
                transactionInfo
        );
    }
}

class SecretProofTransactionMapping extends TransactionMapping {

    @Override
    public SecretProofTransaction apply(JsonObject input) {
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

        JsonObject transaction = input.getAsJsonObject("transaction");
        DeadlineBP deadline = new DeadlineBP(extractBigInteger(transaction.getAsJsonArray("deadline")));
        NetworkType networkType = extractNetworkType(transaction.get("version"));

        return new SecretProofTransaction(
                networkType,
                extractTransactionVersion(transaction.get("version")),
                deadline,
                extractFee(transaction),
                HashType.rawValueOf(transaction.get("hashAlgorithm").getAsInt()),
                transaction.get("secret").getAsString(),
                transaction.get("proof").getAsString(),
                transaction.get("signature").getAsString(),
                new PublicAccount(transaction.get("signer").getAsString(), networkType),
                transactionInfo
        );
    }
}

