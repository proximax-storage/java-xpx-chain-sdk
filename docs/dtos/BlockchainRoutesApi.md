# BlockchainRoutesApi

All URIs are relative to *http://localhost:3000*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getBlockByHeight**](BlockchainRoutesApi.md#getBlockByHeight) | **GET** /block/{height} | Get block information
[**getBlockReceipts**](BlockchainRoutesApi.md#getBlockReceipts) | **GET** /block/{height}/receipts | Get receipts from a block
[**getBlockTransactions**](BlockchainRoutesApi.md#getBlockTransactions) | **GET** /block/{height}/transactions | Get transactions from a block
[**getBlockchainHeight**](BlockchainRoutesApi.md#getBlockchainHeight) | **GET** /chain/height | Get the current height of the chain
[**getBlockchainScore**](BlockchainRoutesApi.md#getBlockchainScore) | **GET** /chain/score | Get the current score of the chain
[**getBlocksByHeightWithLimit**](BlockchainRoutesApi.md#getBlocksByHeightWithLimit) | **GET** /blocks/{height}/limit/{limit} | Get blocks information
[**getDiagnosticStorage**](BlockchainRoutesApi.md#getDiagnosticStorage) | **GET** /diagnostic/storage | Get the storage information
[**getMerkleReceipts**](BlockchainRoutesApi.md#getMerkleReceipts) | **GET** /block/{height}/receipt/{hash}/merkle | Get the merkle path for a given a receipt statement hash and block
[**getMerkleTransaction**](BlockchainRoutesApi.md#getMerkleTransaction) | **GET** /block/{height}/transaction/{hash}/merkle | Get the merkle path for a given a transaction and block


<a name="getBlockByHeight"></a>
# **getBlockByHeight**
> BlockInfoDTO getBlockByHeight(height)

Get block information

Gets a block from the chain that has the given height.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.BlockchainRoutesApi;


BlockchainRoutesApi apiInstance = new BlockchainRoutesApi();
Long height = 789L; // Long | The height of the block.
try {
    BlockInfoDTO result = apiInstance.getBlockByHeight(height);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BlockchainRoutesApi#getBlockByHeight");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **height** | **Long**| The height of the block. |

### Return type

[**BlockInfoDTO**](BlockInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getBlockReceipts"></a>
# **getBlockReceipts**
> List&lt;Object&gt; getBlockReceipts(height)

Get receipts from a block

Returns the [receipts](https://bcdocs.xpxsirius.io/concepts/receipt.html) linked to a block.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.BlockchainRoutesApi;


BlockchainRoutesApi apiInstance = new BlockchainRoutesApi();
Long height = 789L; // Long | The height of the block.
try {
    List<Object> result = apiInstance.getBlockReceipts(height);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BlockchainRoutesApi#getBlockReceipts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **height** | **Long**| The height of the block. |

### Return type

**List&lt;Object&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getBlockTransactions"></a>
# **getBlockTransactions**
> List&lt;Object&gt; getBlockTransactions(height, pageSize, id)

Get transactions from a block

Returns an array of [transactions](https://bcdocs.xpxsirius.io/concepts/transaction.html) included in a block for a given block height.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.BlockchainRoutesApi;


BlockchainRoutesApi apiInstance = new BlockchainRoutesApi();
Long height = 789L; // Long | The height of the block.
Integer pageSize = 10; // Integer | The number of transactions to return for each request.
String id = "id_example"; // String | The transaction id up to which transactions are returned.
try {
    List<Object> result = apiInstance.getBlockTransactions(height, pageSize, id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BlockchainRoutesApi#getBlockTransactions");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **height** | **Long**| The height of the block. |
 **pageSize** | **Integer**| The number of transactions to return for each request. | [optional] [default to 10]
 **id** | **String**| The transaction id up to which transactions are returned. | [optional]

### Return type

**List&lt;Object&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getBlockchainHeight"></a>
# **getBlockchainHeight**
> HeightDTO getBlockchainHeight()

Get the current height of the chain

Returns the current height of the blockchain.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.BlockchainRoutesApi;


BlockchainRoutesApi apiInstance = new BlockchainRoutesApi();
try {
    HeightDTO result = apiInstance.getBlockchainHeight();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BlockchainRoutesApi#getBlockchainHeight");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**HeightDTO**](HeightDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getBlockchainScore"></a>
# **getBlockchainScore**
> BlockchainScoreDTO getBlockchainScore()

Get the current score of the chain

Gets the current score of the blockchain. The higher the score, the better the chain. During synchronization, nodes try to get the best blockchain in the network.  The score for a block is derived from its difficulty and the time (in seconds) that has elapsed since the last block:      block score &#x3D; difficulty − time elasped since last block 

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.BlockchainRoutesApi;


BlockchainRoutesApi apiInstance = new BlockchainRoutesApi();
try {
    BlockchainScoreDTO result = apiInstance.getBlockchainScore();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BlockchainRoutesApi#getBlockchainScore");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**BlockchainScoreDTO**](BlockchainScoreDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getBlocksByHeightWithLimit"></a>
# **getBlocksByHeightWithLimit**
> List&lt;BlockInfoDTO&gt; getBlocksByHeightWithLimit(height, limit)

Get blocks information

Gets up to limit number of blocks after given block height.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.BlockchainRoutesApi;


BlockchainRoutesApi apiInstance = new BlockchainRoutesApi();
Long height = 789L; // Long | The height of the block. If height -1 is not a multiple of the limit provided, the inferior closest multiple + 1 is used instead.
Integer limit = 56; // Integer | The number of blocks to be returned.
try {
    List<BlockInfoDTO> result = apiInstance.getBlocksByHeightWithLimit(height, limit);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BlockchainRoutesApi#getBlocksByHeightWithLimit");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **height** | **Long**| The height of the block. If height -1 is not a multiple of the limit provided, the inferior closest multiple + 1 is used instead. |
 **limit** | **Integer**| The number of blocks to be returned. |

### Return type

[**List&lt;BlockInfoDTO&gt;**](BlockInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getDiagnosticStorage"></a>
# **getDiagnosticStorage**
> BlockchainStorageInfoDTO getDiagnosticStorage()

Get the storage information

Returns statistical information about the blockchain.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.BlockchainRoutesApi;


BlockchainRoutesApi apiInstance = new BlockchainRoutesApi();
try {
    BlockchainStorageInfoDTO result = apiInstance.getDiagnosticStorage();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BlockchainRoutesApi#getDiagnosticStorage");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**BlockchainStorageInfoDTO**](BlockchainStorageInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getMerkleReceipts"></a>
# **getMerkleReceipts**
> MerkleProofInfoDTO getMerkleReceipts(height, hash)

Get the merkle path for a given a receipt statement hash and block

Returns the merkle path for a [receipt statement or resolution](https://bcdocs.xpxsirius.io/concepts/receipt.html) linked to a block. The path is the complementary data needed to calculate the merkle root. A client can compare if the calculated root equals the one recorded in the block header, verifying that the receipt was linked with the block.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.BlockchainRoutesApi;


BlockchainRoutesApi apiInstance = new BlockchainRoutesApi();
Long height = 789L; // Long | The height of the block.
String hash = "hash_example"; // String | The hash of the receipt statement or resolution.
try {
    MerkleProofInfoDTO result = apiInstance.getMerkleReceipts(height, hash);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BlockchainRoutesApi#getMerkleReceipts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **height** | **Long**| The height of the block. |
 **hash** | **String**| The hash of the receipt statement or resolution. |

### Return type

[**MerkleProofInfoDTO**](MerkleProofInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getMerkleTransaction"></a>
# **getMerkleTransaction**
> MerkleProofInfoDTO getMerkleTransaction(height, hash)

Get the merkle path for a given a transaction and block

Returns the merkle path for a [transaction](https://bcdocs.xpxsirius.io/concepts/transaction.html) included in a block. The path is the complementary data needed to calculate the merkle root. A client can compare if the calculated root equals the one recorded in the block header, verifying that the transaction was included in the block.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.BlockchainRoutesApi;


BlockchainRoutesApi apiInstance = new BlockchainRoutesApi();
Long height = 789L; // Long | The height of the block.
String hash = "hash_example"; // String | The hash of the transaction.
try {
    MerkleProofInfoDTO result = apiInstance.getMerkleTransaction(height, hash);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BlockchainRoutesApi#getMerkleTransaction");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **height** | **Long**| The height of the block. |
 **hash** | **String**| The hash of the transaction. |

### Return type

[**MerkleProofInfoDTO**](MerkleProofInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

