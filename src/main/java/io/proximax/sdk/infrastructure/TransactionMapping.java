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

import static io.proximax.sdk.utils.dto.TransactionMappingUtils.extractFee;
import static io.proximax.sdk.utils.dto.TransactionMappingUtils.extractNetworkType;
import static io.proximax.sdk.utils.dto.TransactionMappingUtils.extractTransactionVersion;

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
import io.proximax.sdk.model.blockchain.BlockchainVersion;
import io.proximax.sdk.model.blockchain.NetworkType;
import io.proximax.sdk.model.exchange.AddExchangeOffer;
import io.proximax.sdk.model.exchange.ExchangeOffer;
import io.proximax.sdk.model.exchange.ExchangeOfferType;
import io.proximax.sdk.model.exchange.RemoveExchangeOffer;
import io.proximax.sdk.model.metadata.MetadataModification;
import io.proximax.sdk.model.metadata.MetadataModificationType;
import io.proximax.sdk.model.metadata.MetadataType;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.proximax.sdk.model.mosaic.MosaicNonce;
import io.proximax.sdk.model.mosaic.MosaicProperties;
import io.proximax.sdk.model.mosaic.MosaicPropertyId;
import io.proximax.sdk.model.mosaic.MosaicSupplyType;
import io.proximax.sdk.model.namespace.NamespaceId;
import io.proximax.sdk.model.namespace.NamespaceType;
import io.proximax.sdk.model.transaction.*;
import io.proximax.sdk.utils.GsonUtils;
import io.reactivex.functions.Function;

public class TransactionMapping implements Function<JsonObject, Transaction> {
   @Override
   public Transaction apply(JsonObject input) {
      try {
         JsonObject transaction = input.getAsJsonObject("transaction");
         int type = transaction.get("type").getAsInt();

         switch (EntityType.rawValueOf(type)) {
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
         case ACCOUNT_PROPERTIES_ADDRESS:
         case ACCOUNT_PROPERTIES_MOSAIC:
         case ACCOUNT_PROPERTIES_ENTITY_TYPE:
            return new ModifyAccountPropertiesTransactionMapping().apply(input);
         case ACCOUNT_LINK:
            return new AccountLinkTransactionMapping().apply(input);
         case BLOCKCHAIN_UPGRADE:
            return new BlockchainUpgradeTransactionMapping().apply(input);
         case BLOCKCHAIN_CONFIG:
            return new BlockchainConfigTransactionMapping().apply(input);
         case EXCHANGE_OFFER_ADD:
            return new ExchangeOfferAddTransactionMapping().apply(input);
         case EXCHANGE_OFFER_REMOVE:
            return new ExchangeOfferRemoveTransactionMapping().apply(input);
         case EXCHANGE_OFFER:
            return new ExchangeOfferTransactionMapping().apply(input);
         case DRIVE_PREPARE:
            return new DrivePrepareTransactionMapping().apply(input);
         default:
            throw new UnsupportedOperationException("Unimplemented transaction type " + type);
         }
      } catch (RuntimeException e) {
         throw new RuntimeException("Failed to map transaction " + input, e);
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
         return TransactionInfo.create(GsonUtils.getBigInteger(jsonObject.getAsJsonArray("height")),
               jsonObject.get("index").getAsInt(),
               jsonObject.get("id").getAsString(),
               jsonObject.get("hash").getAsString(),
               jsonObject.get("merkleComponentHash").getAsString());
      }
      // agrregateHash and id indicate aggregate transaction
      if (jsonObject.has("aggregateHash") && jsonObject.has("id")) {
         return TransactionInfo.createAggregate(GsonUtils.getBigInteger(jsonObject.getAsJsonArray("height")),
               jsonObject.get("index").getAsInt(),
               jsonObject.get("id").getAsString(),
               jsonObject.get("aggregateHash").getAsString(),
               jsonObject.get("aggregateId").getAsString());
      }
      // transaction with missing id
      return TransactionInfo.create(GsonUtils.getBigInteger(jsonObject.getAsJsonArray("height")),
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
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      // mosaics
      List<Mosaic> mosaics;
      if (transaction.getAsJsonArray("mosaics") != null) {
         mosaics = stream(transaction.getAsJsonArray("mosaics")).map(item -> (JsonObject) item)
               .map(mosaic -> new Mosaic(new MosaicId(GsonUtils.getBigInteger(mosaic.getAsJsonArray("id"))),
                     GsonUtils.getBigInteger(mosaic.getAsJsonArray("amount"))))
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
         message = PlainMessage.EMPTY;
      }

      // version
      JsonElement version = transaction.get("version");
      // create transfer transaction instance
      return new TransferTransaction(
            extractNetworkType(version), 
            extractTransactionVersion(version), 
            deadline,
            extractFee(transaction),
            Optional.of(transaction.get("signature").getAsString()),
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), extractNetworkType(version))), 
            Optional.of(transactionInfo),
            Recipient.from(Address.createFromEncoded(transaction.get("recipient").getAsString())), 
            mosaics, 
            message);
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
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      // version
      JsonElement version = transaction.get("version");
      // transaction type
      EntityType type = EntityType.rawValueOf(transaction.get("type").getAsInt());
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
               deadline, extractFee(transaction), Optional.of(signature), Optional.of(signer),
               Optional.of(transactionInfo), metadataType, Optional.empty(),
               Optional.of(Address.createFromEncoded(transaction.get("metadataId").getAsString())), modifications);
      case MODIFY_MOSAIC_METADATA:
         return new ModifyMetadataTransaction(type, extractNetworkType(version), extractTransactionVersion(version),
               deadline, extractFee(transaction), Optional.of(signature), Optional.of(signer),
               Optional.of(transactionInfo), metadataType,
               Optional.of(new MosaicId(GsonUtils.getBigInteger(transaction.getAsJsonArray("metadataId")))),
               Optional.empty(), modifications);
      case MODIFY_NAMESPACE_METADATA:
         return new ModifyMetadataTransaction(type, extractNetworkType(version), extractTransactionVersion(version),
               deadline, extractFee(transaction), Optional.of(signature), Optional.of(signer),
               Optional.of(transactionInfo), metadataType,
               Optional.of(new NamespaceId(GsonUtils.getBigInteger(transaction.getAsJsonArray("metadataId")))),
               Optional.empty(), modifications);
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
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      // version
      JsonElement version = transaction.get("version");
      // transaction type
      EntityType type = EntityType.rawValueOf(transaction.get("type").getAsInt());
      // signer
      PublicAccount signer = new PublicAccount(transaction.get("signer").getAsString(), extractNetworkType(version));
      // signature
      String signature = transaction.get("signature").getAsString();
      // metadata type
      AccountPropertyType propertyType = AccountPropertyType.getByCode(transaction.get("propertyType").getAsInt());
      // create instance of transaction
      switch (type) {
      case ACCOUNT_PROPERTIES_ADDRESS:
         return new ModifyAccountPropertyTransaction.AddressModification(extractNetworkType(version), extractTransactionVersion(version), deadline, extractFee(transaction), 
               Optional.of(signature), Optional.of(signer), Optional.of(transactionInfo), 
               propertyType, getAddressMods(transaction));
      case ACCOUNT_PROPERTIES_MOSAIC:
         return new ModifyAccountPropertyTransaction.MosaicModification(extractNetworkType(version), extractTransactionVersion(version), deadline, extractFee(transaction), 
               Optional.of(signature), Optional.of(signer), Optional.of(transactionInfo), 
               propertyType, getMosaicMods(transaction));
      case ACCOUNT_PROPERTIES_ENTITY_TYPE:
         return new ModifyAccountPropertyTransaction.EntityTypeModification(extractNetworkType(version), extractTransactionVersion(version), deadline, extractFee(transaction), 
               Optional.of(signature), Optional.of(signer), Optional.of(transactionInfo), 
               propertyType, getEntityTypeMods(transaction));
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
               .getByCode(Hacks.getType(json).getAsInt());
         return new AccountPropertyModification<>(modType, Address.createFromEncoded(json.get("value").getAsString()));
      }).collect(Collectors.toList());
   }

   /**
    * create list of mosaic account property modifications
    * 
    * @param transaction the transaction object to get data from
    * @return the list of mosaic account property modifications
    */
   static List<AccountPropertyModification<UInt64Id>> getMosaicMods(JsonObject transaction) {
      return stream(transaction.getAsJsonArray("modifications"))
            .map(obj -> (JsonObject) obj)
            .map(json -> {
               AccountPropertyModificationType modType = AccountPropertyModificationType.getByCode(Hacks.getType(json).getAsInt());
               return new AccountPropertyModification<>(modType, (UInt64Id)new MosaicId(GsonUtils.getBigInteger(json.get("value").getAsJsonArray())));
            }).collect(Collectors.toList());
   }

   /**
    * create list of entity type account property modifications
    * 
    * @param transaction the transaction object to get data from
    * @return the list of entity type account property modifications
    */
   static List<AccountPropertyModification<EntityType>> getEntityTypeMods(JsonObject transaction) {
      return stream(transaction.getAsJsonArray("modifications")).map(obj -> (JsonObject) obj).map(json -> {
         AccountPropertyModificationType modType = AccountPropertyModificationType
               .getByCode(Hacks.getType(json).getAsInt());
         return new AccountPropertyModification<>(modType, EntityType.rawValueOf(json.get("value").getAsInt()));
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
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      NamespaceType namespaceType = NamespaceType.rawValueOf(transaction.get("namespaceType").getAsInt());
      NamespaceId namespaceId = new NamespaceId(GsonUtils.getBigInteger(transaction.getAsJsonArray("namespaceId")));
      Optional<BigInteger> namespaceDuration = namespaceType == NamespaceType.ROOT_NAMESPACE
            ? Optional.of(GsonUtils.getBigInteger(transaction.getAsJsonArray("duration")))
            : Optional.empty();
      Optional<NamespaceId> namespaceParentId = namespaceType == NamespaceType.SUB_NAMESPACE
            ? Optional.of(new NamespaceId(GsonUtils.getBigInteger(transaction.getAsJsonArray("parentId"))))
            : Optional.empty();
      JsonElement version = transaction.get("version");
      // return the register namespace transaction
      return new RegisterNamespaceTransaction(extractNetworkType(version), extractTransactionVersion(version), deadline,
            extractFee(transaction), Optional.of(transaction.get("signature").getAsString()),
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), extractNetworkType(version))),
            Optional.of(transactionInfo), transaction.get("name").getAsString(), namespaceId, namespaceDuration,
            namespaceParentId, namespaceType);
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
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      // construct properties
      JsonElement version = transaction.get("version");
      // return instance of mosaic definition transaction
      return new MosaicDefinitionTransaction(extractNetworkType(version), extractTransactionVersion(version), deadline,
            extractFee(transaction), Optional.of(transaction.get("signature").getAsString()),
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), extractNetworkType(version))),
            Optional.of(transactionInfo), Hacks.extractNonce(transaction),
            new MosaicId(GsonUtils.getBigInteger(transaction.getAsJsonArray("mosaicId"))),
            extractProperties(transaction.getAsJsonArray("properties")));
   }

   private static MosaicProperties extractProperties(JsonArray props) {
      // get property values
      BigInteger flags = getProperty(props, MosaicPropertyId.FLAGS)
            .orElseThrow(() -> new IllegalStateException("flags property is mandatory"));
      BigInteger divisibility = getProperty(props, MosaicPropertyId.DIVISIBILITY)
            .orElseThrow(() -> new IllegalStateException("divisibility property is mandatory"));
      Optional<BigInteger> duration = getProperty(props, MosaicPropertyId.DURATION);
      return MosaicProperties.create(flags.intValue(), divisibility.intValue(), duration);
   }

   /**
    * get first property with specified id. This implementation is order-agnostic and properties can be in any order
    * 
    * @param mosaicProperties the properties to search in
    * @param id the id to search for
    * @return the optional BigInteger with property value
    */
   private static Optional<BigInteger> getProperty(JsonArray mosaicProperties, MosaicPropertyId id) {
      final byte index = id.getCode();
      return GsonUtils.stream(mosaicProperties).map(JsonElement::getAsJsonObject)
            .filter(obj -> (obj.has("key") ? obj.get("key") : obj.get("id")).getAsByte() == index)
            .map(obj -> obj.getAsJsonArray("value")).map(GsonUtils::getBigInteger).findFirst();
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
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      JsonElement version = transaction.get("version");
      // return instance of mosaic alias definition transaction
      return new AliasTransaction(EntityType.MOSAIC_ALIAS, extractNetworkType(version),
            extractTransactionVersion(version), deadline, extractFee(transaction),
            Optional.of(transaction.get("signature").getAsString()),
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), extractNetworkType(version))),
            Optional.of(transactionInfo), 
            Optional.of(new MosaicId(GsonUtils.getBigInteger(transaction.getAsJsonArray("mosaicId")))),
            Optional.empty(), new NamespaceId(GsonUtils.getBigInteger(transaction.getAsJsonArray("namespaceId"))),
            AliasAction.getByCode(transaction.get("aliasAction").getAsInt()));
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
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      JsonElement version = transaction.get("version");
      // return instance of mosaic alias definition transaction
      return new AliasTransaction(EntityType.ADDRESS_ALIAS, extractNetworkType(version),
            extractTransactionVersion(version), deadline, extractFee(transaction),
            Optional.of(transaction.get("signature").getAsString()),
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), extractNetworkType(version))),
            Optional.of(transactionInfo), Optional.empty(),
            Optional.of(Address.createFromEncoded(transaction.get("address").getAsString())),
            new NamespaceId(GsonUtils.getBigInteger(transaction.getAsJsonArray("namespaceId"))),
            AliasAction.getByCode(transaction.get("aliasAction").getAsInt()));
   }
}

class MosaicSupplyChangeTransactionMapping extends TransactionMapping {

   @Override
   public MosaicSupplyChangeTransaction apply(JsonObject input) {
      TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

      JsonObject transaction = input.getAsJsonObject("transaction");
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));

      return new MosaicSupplyChangeTransaction(extractNetworkType(transaction.get("version")),
            extractTransactionVersion(transaction.get("version")), deadline, extractFee(transaction),
            Optional.of(transaction.get("signature").getAsString()),
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(),
                  extractNetworkType(transaction.get("version")))),
            Optional.of(transactionInfo), new MosaicId(GsonUtils.getBigInteger(transaction.getAsJsonArray("mosaicId"))),
            MosaicSupplyType.rawValueOf(transaction.get("direction").getAsInt()),
            GsonUtils.getBigInteger(transaction.getAsJsonArray("delta")));
   }
}

class MultisigModificationTransactionMapping extends TransactionMapping {

   @Override
   public ModifyMultisigAccountTransaction apply(JsonObject input) {
      TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

      JsonObject transaction = input.getAsJsonObject("transaction");
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      NetworkType networkType = extractNetworkType(transaction.get("version"));

      List<MultisigCosignatoryModification> modifications = transaction.has("modifications")
            ? stream(transaction.getAsJsonArray("modifications")).map(item -> (JsonObject) item)
                  .map(multisigModification -> new MultisigCosignatoryModification(
                        MultisigCosignatoryModificationType.rawValueOf(multisigModification.get("type").getAsInt()),
                        PublicAccount.createFromPublicKey(
                              multisigModification.get("cosignatoryPublicKey").getAsString(),
                              networkType)))
                  .collect(Collectors.toList())
            : Collections.emptyList();

      return new ModifyMultisigAccountTransaction(networkType, extractTransactionVersion(transaction.get("version")),
            deadline, extractFee(transaction), Optional.of(transaction.get("signature").getAsString()),
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), networkType)),
            Optional.of(transactionInfo), transaction.get("minApprovalDelta").getAsInt(),
            transaction.get("minRemovalDelta").getAsInt(), modifications);

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
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      NetworkType networkType = extractNetworkType(transaction.get("version"));

      return new ModifyContractTransaction(networkType, extractTransactionVersion(transaction.get("version")), deadline,
            extractFee(transaction), Optional.of(transaction.get("signature").getAsString()),
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), networkType)),
            Optional.of(transactionInfo), GsonUtils.getBigInteger(transaction.get("durationDelta").getAsJsonArray()),
            transaction.get("hash").getAsString(), extractModifications(networkType, transaction, "customers"),
            extractModifications(networkType, transaction, "executors"),
            extractModifications(networkType, transaction, "verifiers"));
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
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      NetworkType networkType = extractNetworkType(transaction.get("version"));

      List<Transaction> transactions = new ArrayList<>();
      for (int i = 0; i < transaction.getAsJsonArray("transactions").size(); i++) {
         JsonObject innerTransaction = transaction.getAsJsonArray("transactions").get(i).getAsJsonObject();
         innerTransaction.getAsJsonObject("transaction").add("deadline", transaction.getAsJsonArray("deadline"));
         Hacks.setFee(innerTransaction.getAsJsonObject("transaction"), transaction);
         innerTransaction.getAsJsonObject("transaction").add("fee", transaction.getAsJsonArray("fee"));
         innerTransaction.getAsJsonObject("transaction").add("signature", transaction.get("signature"));
         if (!innerTransaction.has("meta")) {
            innerTransaction.add("meta", input.getAsJsonObject("meta"));
         }
         transactions.add(new TransactionMapping().apply(innerTransaction));
      }

      List<AggregateTransactionCosignature> cosignatures = new ArrayList<>();
      if (transaction.getAsJsonArray("cosignatures") != null) {
         cosignatures = stream(transaction.getAsJsonArray("cosignatures")).map(item -> (JsonObject) item)
               .map(aggregateCosignature -> new AggregateTransactionCosignature(
                     aggregateCosignature.get("signature").getAsString(),
                     new PublicAccount(aggregateCosignature.get("signer").getAsString(), networkType)))
               .collect(Collectors.toList());
      }
      return new AggregateTransaction(
            EntityType.rawValueOf(transaction.get("type").getAsInt()),
            networkType, 
            extractTransactionVersion(transaction.get("version")), 
            deadline, 
            extractFee(transaction), 
            Optional.of(transaction.get("signature").getAsString()),
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), networkType)), 
            Optional.of(transactionInfo), 
            transactions,
            cosignatures);
   }
}

class LockFundsTransactionMapping extends TransactionMapping {

   @Override
   public LockFundsTransaction apply(JsonObject input) {
      TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

      JsonObject transaction = input.getAsJsonObject("transaction");
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      NetworkType networkType = extractNetworkType(transaction.get("version"));
      Mosaic mosaic;
      if (transaction.has("mosaicId")) {
         mosaic = new Mosaic(new MosaicId(GsonUtils.getBigInteger(transaction.getAsJsonArray("mosaicId"))),
               GsonUtils.getBigInteger(transaction.getAsJsonArray("amount")));
      } else {
         mosaic = new Mosaic(
               new MosaicId(GsonUtils.getBigInteger(transaction.getAsJsonObject("mosaic").getAsJsonArray("id"))),
               GsonUtils.getBigInteger(transaction.getAsJsonObject("mosaic").getAsJsonArray("amount")));
      }
      return new LockFundsTransaction(networkType, 
            extractTransactionVersion(transaction.get("version")), 
            deadline,
            extractFee(transaction),
            Optional.of(transaction.get("signature").getAsString()),
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), networkType)),
            Optional.of(transactionInfo),
            mosaic,
            GsonUtils.getBigInteger(transaction.getAsJsonArray("duration")),
            new SignedTransaction("", transaction.get("hash").getAsString(), EntityType.AGGREGATE_BONDED));
   }
}

class SecretLockTransactionMapping extends TransactionMapping {

   @Override
   public SecretLockTransaction apply(JsonObject input) {
      TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

      JsonObject transaction = input.getAsJsonObject("transaction");
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      NetworkType networkType = extractNetworkType(transaction.get("version"));
      Mosaic mosaic;
      if (transaction.has("mosaicId")) {
         mosaic = new Mosaic(new MosaicId(GsonUtils.getBigInteger(transaction.getAsJsonArray("mosaicId"))),
               GsonUtils.getBigInteger(transaction.getAsJsonArray("amount")));
      } else {
         mosaic = new Mosaic(
               new MosaicId(GsonUtils.getBigInteger(transaction.getAsJsonObject("mosaic").getAsJsonArray("id"))),
               GsonUtils.getBigInteger(transaction.getAsJsonObject("mosaic").getAsJsonArray("amount")));
      }
      return new SecretLockTransaction(networkType, extractTransactionVersion(transaction.get("version")), deadline,
            extractFee(transaction), Optional.of(transaction.get("signature").getAsString()),
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), networkType)),
            Optional.of(transactionInfo), mosaic, GsonUtils.getBigInteger(transaction.getAsJsonArray("duration")),
            HashType.rawValueOf(transaction.get("hashAlgorithm").getAsInt()), transaction.get("secret").getAsString(),
            Address.createFromEncoded(transaction.get("recipient").getAsString()));
   }
}

class SecretProofTransactionMapping extends TransactionMapping {

   @Override
   public SecretProofTransaction apply(JsonObject input) {
      TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

      JsonObject transaction = input.getAsJsonObject("transaction");
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      NetworkType networkType = extractNetworkType(transaction.get("version"));

      return new SecretProofTransaction(networkType, extractTransactionVersion(transaction.get("version")), deadline,
            extractFee(transaction), Optional.of(transaction.get("signature").getAsString()),
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), networkType)),
            Optional.of(transactionInfo), HashType.rawValueOf(transaction.get("hashAlgorithm").getAsInt()),
            transaction.get("secret").getAsString(), transaction.get("proof").getAsString(),
            Recipient.from(Address.createFromEncoded(transaction.get("recipient").getAsString())));
   }

}

/**
 * Mapping from server response to an account link transaction instance
 */
class AccountLinkTransactionMapping extends TransactionMapping {

   @Override
   public AccountLinkTransaction apply(JsonObject input) {
      TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

      JsonObject transaction = input.getAsJsonObject("transaction");
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      NetworkType networkType = extractNetworkType(transaction.get("version"));
      return new AccountLinkTransaction(
            networkType, 
            extractTransactionVersion(transaction.get("version")), 
            deadline,
            extractFee(transaction), 
            Optional.of(transaction.get("signature").getAsString()),
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), networkType)),
            Optional.of(transactionInfo), 
            new PublicAccount(transaction.get("remoteAccountKey").getAsString(), networkType),
            AccountLinkAction.getByCode(Hacks.getAction(transaction).getAsByte()));
   }
}

/**
 * Mapping from server response to an blockchain upgrade transaction
 */
class BlockchainUpgradeTransactionMapping extends TransactionMapping {

   @Override
   public BlockchainUpgradeTransaction apply(JsonObject input) {
      TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

      JsonObject transaction = input.getAsJsonObject("transaction");
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      NetworkType networkType = extractNetworkType(transaction.get("version"));
      return new BlockchainUpgradeTransaction(
            networkType, 
            extractTransactionVersion(transaction.get("version")), 
            deadline, 
            extractFee(transaction), 
            Optional.of(transaction.get("signature").getAsString()), 
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), networkType)), 
            Optional.of(transactionInfo), 
            GsonUtils.getBigInteger(transaction.getAsJsonArray("upgradePeriod")), 
            BlockchainVersion.fromVersionValue(GsonUtils.getBigInteger(Hacks.getNewVersion(transaction))));
   }
}

/**
 * Mapping from server response to an blockchain upgrade transaction
 */
class BlockchainConfigTransactionMapping extends TransactionMapping {

   @Override
   public BlockchainConfigTransaction apply(JsonObject input) {
      TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

      JsonObject transaction = input.getAsJsonObject("transaction");
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      NetworkType networkType = extractNetworkType(transaction.get("version"));
      return new BlockchainConfigTransaction(
            networkType, 
            extractTransactionVersion(transaction.get("version")), 
            deadline, 
            extractFee(transaction), 
            Optional.of(transaction.get("signature").getAsString()), 
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), networkType)), 
            Optional.of(transactionInfo), 
            GsonUtils.getBigInteger(transaction.getAsJsonArray("applyHeightDelta")), 
            getConfigContent(transaction),
            transaction.get("supportedEntityVersions").getAsString());
   }
   
   static String getConfigContent(JsonObject transaction) {
      if (transaction.has("blockChainConfig")) {
         return transaction.get("blockChainConfig").getAsString();
      } else {
         return transaction.get("networkConfig").getAsString();
      }
   }
}

/**
 * Mapping from server response to a transaction
 */
class ExchangeOfferAddTransactionMapping extends TransactionMapping {

   @Override
   public ExchangeOfferAddTransaction apply(JsonObject input) {
      TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

      JsonObject transaction = input.getAsJsonObject("transaction");
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      NetworkType networkType = extractNetworkType(transaction.get("version"));
      return new ExchangeOfferAddTransaction(
            networkType, 
            extractTransactionVersion(transaction.get("version")), 
            deadline, 
            extractFee(transaction), 
            Optional.of(transaction.get("signature").getAsString()), 
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), networkType)), 
            Optional.of(transactionInfo), 
            loadOffers(transaction));
   }
   
   static List<AddExchangeOffer> loadOffers(JsonObject transaction) {
      if (transaction.getAsJsonArray("offers") != null) {
         return stream(transaction.getAsJsonArray("offers"))
               .map(item -> (JsonObject) item)
               .map(json -> new AddExchangeOffer(
                     new MosaicId(Hacks.getBigInt(Hacks.inOfferOrNot(json, "mosaicId"))),
                     Hacks.getBigInt(Hacks.inOfferOrNot(json, "mosaicAmount")),
                     Hacks.getBigInt(Hacks.inOfferOrNot(json, "cost")),
                     ExchangeOfferType.getByCode(Hacks.inOfferOrNot(json, "type").getAsInt()),
                     Hacks.getBigInt(json.getAsJsonArray("duration"))
                     ))
               .collect(Collectors.toList());
      } else {
         return new ArrayList<>();
      }
   }
}

/**
 * Mapping from server response to a transaction
 */
class ExchangeOfferRemoveTransactionMapping extends TransactionMapping {

   @Override
   public ExchangeOfferRemoveTransaction apply(JsonObject input) {
      TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

      JsonObject transaction = input.getAsJsonObject("transaction");
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      NetworkType networkType = extractNetworkType(transaction.get("version"));
      return new ExchangeOfferRemoveTransaction(
            networkType, 
            extractTransactionVersion(transaction.get("version")), 
            deadline, 
            extractFee(transaction), 
            Optional.of(transaction.get("signature").getAsString()), 
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), networkType)), 
            Optional.of(transactionInfo), 
            loadOffers(transaction));
   }
   
   static List<RemoveExchangeOffer> loadOffers(JsonObject transaction) {
      if (transaction.getAsJsonArray("offers") != null) {
         return stream(transaction.getAsJsonArray("offers"))
               .map(item -> (JsonObject) item)
               .map(json -> new RemoveExchangeOffer(
                     new MosaicId(Hacks.getBigInt(Hacks.inOfferOrNot(json, "mosaicId"))),
                     ExchangeOfferType.getByCode(Hacks.inOfferOrNot(json, "type", "offerType").getAsInt())
                     ))
               .collect(Collectors.toList());
      } else {
         return new ArrayList<>();
      }
   }
}

/**
 * Mapping from server response to a transaction
 */
class ExchangeOfferTransactionMapping extends TransactionMapping {

   @Override
   public ExchangeOfferTransaction apply(JsonObject input) {
      TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

      JsonObject transaction = input.getAsJsonObject("transaction");
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      NetworkType networkType = extractNetworkType(transaction.get("version"));
      return new ExchangeOfferTransaction(
            networkType, 
            extractTransactionVersion(transaction.get("version")), 
            deadline, 
            extractFee(transaction), 
            Optional.of(transaction.get("signature").getAsString()), 
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), networkType)), 
            Optional.of(transactionInfo), 
            loadOffers(transaction, networkType));
   }
   
   static List<ExchangeOffer> loadOffers(JsonObject transaction, NetworkType networkType) {
      if (transaction.getAsJsonArray("offers") != null) {
         return stream(transaction.getAsJsonArray("offers"))
               .map(item -> (JsonObject) item)
               .map(json -> new ExchangeOffer(
                     new MosaicId(Hacks.getBigInt(Hacks.inOfferOrNot(json, "mosaicId"))),
                     Hacks.getBigInt(Hacks.inOfferOrNot(json, "mosaicAmount")),
                     Hacks.getBigInt(Hacks.inOfferOrNot(json, "cost")),
                     ExchangeOfferType.getByCode(Hacks.inOfferOrNot(json, "type").getAsInt()),
                     new PublicAccount(json.get("owner").getAsString(), networkType)
                     ))
               .collect(Collectors.toList());
      } else {
         return new ArrayList<>();
      }
   }
}

/**
 * Mapping from server response to a transaction
 */
class DrivePrepareTransactionMapping extends TransactionMapping {

   @Override
   public DrivePrepareTransaction apply(JsonObject input) {
      TransactionInfo transactionInfo = this.createTransactionInfo(input.getAsJsonObject("meta"));

      JsonObject transaction = input.getAsJsonObject("transaction");
      DeadlineRaw deadline = new DeadlineRaw(GsonUtils.getBigInteger(transaction.getAsJsonArray("deadline")));
      NetworkType networkType = extractNetworkType(transaction.get("version"));

      return new DrivePrepareTransaction(
            networkType, 
            extractTransactionVersion(transaction.get("version")), 
            deadline,
            extractFee(transaction), 
            Optional.of(transaction.get("signature").getAsString()),
            Optional.of(new PublicAccount(transaction.get("signer").getAsString(), networkType)),
            Optional.of(transactionInfo), 
            transaction.get("owner").getAsString(),
            GsonUtils.getBigInteger(transaction.get("duration").getAsJsonArray()),
            GsonUtils.getBigInteger(transaction.get("billingPeriod").getAsJsonArray()),
            GsonUtils.getBigInteger(transaction.get("billingPrice").getAsJsonArray()),
            GsonUtils.getBigInteger(transaction.get("driveSize").getAsJsonArray()),
            transaction.get("replicas").getAsInt(),
            transaction.get("minReplicators").getAsInt(),
            transaction.get("percentApprovers").getAsInt()
            );
   }

}

class Hacks {
   static JsonElement inOfferOrNot(JsonObject json, String... names) {
      for (String name : names) {
         if (json.has("offer")) {
            return json.getAsJsonObject("offer").get(name);
         } else if (json.has(name)) {
            return json.get(name);
         }
      }
      throw new IllegalStateException("could not find required field: " + json.toString());
   }
   
   static BigInteger getBigInt(JsonElement json) {
      if (json.isJsonArray()) {
         return GsonUtils.getBigInteger(json.getAsJsonArray());
      } else {
         return json.getAsBigInteger();
      }
   }
   
   /**
    * helper method to support different names of modification type field
    * 
    * @param json modification object
    * @return element representing the modification type
    */
   static JsonElement getType(JsonObject json) {
      if (json.has("type")) {
         return json.get("type");
      } else if (json.has("modificationType")) {
         return json.get("modificationType");
      } else {
         throw new IllegalStateException("No known type field in: " + json.toString());
      }
   }
   
   /**
    * retrieve fee from the transaction. listener returns fee as uint64 "fee" and services return string "maxFee" and
    * this method provides support for both
    * 
    * @param transaction transaction object with fee or maxFee field
    * @return value of the fee
    */
   static MosaicNonce extractNonce(JsonObject transaction) {
      if (transaction.has("mosaicNonce")) {
         return MosaicNonce.createFromBigInteger(transaction.get("mosaicNonce").getAsBigInteger());
      } else if (transaction.has("nonce")) {
         return MosaicNonce.createFromBigInteger(transaction.get("nonce").getAsBigInteger());
      } else {
         throw new IllegalArgumentException("Fee is missing in the transaction description");
      }
   }
   
   /**
    * copy the fee field from the source to target object
    * 
    * @param target JSON object to copy the fee to
    * @param source JSON object to take the fee from
    */
   static void setFee(JsonObject target, JsonObject source) {
      if (source.has("maxFee")) {
         target.add("maxFee", source.get("maxFee"));
      } else if (source.has("fee")) {
         target.add("fee", source.get("fee"));
      } else {
         throw new IllegalArgumentException("Fee is missing in the transaction description");
      }
   }
   
   /**
    * helper method to retrieve account link action as it uses different field names in listener and transaction
    * 
    * @param json transaction object
    * @return JSON element representing the action
    */
   static JsonElement getAction(JsonObject json) {
      if (json.has("action")) {
         return json.get("action");
      } else {
         return json.get("linkAction");
      }
   }
   
   
   static JsonArray getNewVersion(JsonObject transaction) {
      if (transaction.has("newCatapultVersion")) {
         return transaction.getAsJsonArray("newCatapultVersion");
      } else {
         return transaction.getAsJsonArray("newBlockchainVersion");
      }
   }
}