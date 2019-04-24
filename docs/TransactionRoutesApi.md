# TransactionRoutesApi

All URIs are relative to *http://localhost:3000*

Method | HTTP request | Description
------------- | ------------- | -------------
[**announceCosignatureTransaction**](TransactionRoutesApi.md#announceCosignatureTransaction) | **PUT** /transaction/cosignature | Announce a cosignature transaction
[**announcePartialTransaction**](TransactionRoutesApi.md#announcePartialTransaction) | **PUT** /transaction/partial | Announce an aggregate bonded transaction
[**announceTransaction**](TransactionRoutesApi.md#announceTransaction) | **PUT** /transaction | Announce a new transaction
[**getTransaction**](TransactionRoutesApi.md#getTransaction) | **GET** /transaction/{transactionId} | Get transaction information
[**getTransactionStatus**](TransactionRoutesApi.md#getTransactionStatus) | **GET** /transaction/{hash}/status | Get transaction status
[**getTransactions**](TransactionRoutesApi.md#getTransactions) | **POST** /transaction | Get transactions information
[**getTransactionsStatuses**](TransactionRoutesApi.md#getTransactionsStatuses) | **POST** /transaction/statuses | Get transactions status.


<a name="announceCosignatureTransaction"></a>
# **announceCosignatureTransaction**
> Object announceCosignatureTransaction(payload)

Announce a cosignature transaction

Announces a [cosignature transaction](https://nemtech.github.io/concepts/aggregate-transaction.html#cosignature-transaction) to the network.

### Example
```java
// Import classes:
//import io.nem.sdk.api.ApiException;
//import io.nem.sdk.api.swagger.TransactionRoutesApi;


TransactionRoutesApi apiInstance = new TransactionRoutesApi();
TransactionPayload payload = new TransactionPayload(); // TransactionPayload | The transaction [payload](https://nemtech.github.io/api.html#serialization).
try {
    Object result = apiInstance.announceCosignatureTransaction(payload);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TransactionRoutesApi#announceCosignatureTransaction");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **payload** | [**TransactionPayload**](TransactionPayload.md)| The transaction [payload](https://nemtech.github.io/api.html#serialization). |

### Return type

**Object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="announcePartialTransaction"></a>
# **announcePartialTransaction**
> Object announcePartialTransaction(payload)

Announce an aggregate bonded transaction

Announces an [aggregate bonded transaction](https://nemtech.github.io/concepts/aggregate-transaction.html#aggregate-bonded) to the network.

### Example
```java
// Import classes:
//import io.nem.sdk.api.ApiException;
//import io.nem.sdk.api.swagger.TransactionRoutesApi;


TransactionRoutesApi apiInstance = new TransactionRoutesApi();
TransactionPayload payload = new TransactionPayload(); // TransactionPayload | The transaction [payload](https://nemtech.github.io/api.html#serialization).
try {
    Object result = apiInstance.announcePartialTransaction(payload);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TransactionRoutesApi#announcePartialTransaction");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **payload** | [**TransactionPayload**](TransactionPayload.md)| The transaction [payload](https://nemtech.github.io/api.html#serialization). |

### Return type

**Object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="announceTransaction"></a>
# **announceTransaction**
> Object announceTransaction(payload)

Announce a new transaction

Announces a transaction to the network. It is recommended to use the NEM2-SDK to announce transactions as they should be [serialized](https://nemtech.github.io/api.html#serialization).

### Example
```java
// Import classes:
//import io.nem.sdk.api.ApiException;
//import io.nem.sdk.api.swagger.TransactionRoutesApi;


TransactionRoutesApi apiInstance = new TransactionRoutesApi();
TransactionPayload payload = new TransactionPayload(); // TransactionPayload | The transaction [payload](https://nemtech.github.io/api.html#serialization).
try {
    Object result = apiInstance.announceTransaction(payload);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TransactionRoutesApi#announceTransaction");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **payload** | [**TransactionPayload**](TransactionPayload.md)| The transaction [payload](https://nemtech.github.io/api.html#serialization). |

### Return type

**Object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getTransaction"></a>
# **getTransaction**
> Object getTransaction(transactionId)

Get transaction information

Returns transaction information given a transactionId or hash.

### Example
```java
// Import classes:
//import io.nem.sdk.api.ApiException;
//import io.nem.sdk.api.swagger.TransactionRoutesApi;


TransactionRoutesApi apiInstance = new TransactionRoutesApi();
String transactionId = "transactionId_example"; // String | The transaction id or hash.
try {
    Object result = apiInstance.getTransaction(transactionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TransactionRoutesApi#getTransaction");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **transactionId** | **String**| The transaction id or hash. |

### Return type

**Object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getTransactionStatus"></a>
# **getTransactionStatus**
> TransactionStatusDTO getTransactionStatus(hash)

Get transaction status

Returns the transaction status for a given hash.

### Example
```java
// Import classes:
//import io.nem.sdk.api.ApiException;
//import io.nem.sdk.api.swagger.TransactionRoutesApi;


TransactionRoutesApi apiInstance = new TransactionRoutesApi();
String hash = "hash_example"; // String | The transaction hash.
try {
    TransactionStatusDTO result = apiInstance.getTransactionStatus(hash);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TransactionRoutesApi#getTransactionStatus");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **hash** | **String**| The transaction hash. |

### Return type

[**TransactionStatusDTO**](TransactionStatusDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getTransactions"></a>
# **getTransactions**
> List&lt;Object&gt; getTransactions(transactionIds)

Get transactions information

Returns transactions information for a given array of transactionIds.

### Example
```java
// Import classes:
//import io.nem.sdk.api.ApiException;
//import io.nem.sdk.api.swagger.TransactionRoutesApi;


TransactionRoutesApi apiInstance = new TransactionRoutesApi();
TransactionIds transactionIds = new TransactionIds(); // TransactionIds | An array of transaction ids or hashes.
try {
    List<Object> result = apiInstance.getTransactions(transactionIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TransactionRoutesApi#getTransactions");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **transactionIds** | [**TransactionIds**](TransactionIds.md)| An array of transaction ids or hashes. |

### Return type

**List&lt;Object&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getTransactionsStatuses"></a>
# **getTransactionsStatuses**
> List&lt;TransactionStatusDTO&gt; getTransactionsStatuses(transactionHashes)

Get transactions status.

Returns an array of transaction statuses for a given array of transaction hashes.

### Example
```java
// Import classes:
//import io.nem.sdk.api.ApiException;
//import io.nem.sdk.api.swagger.TransactionRoutesApi;


TransactionRoutesApi apiInstance = new TransactionRoutesApi();
TransactionHashes transactionHashes = new TransactionHashes(); // TransactionHashes | An array of transaction hashes.
try {
    List<TransactionStatusDTO> result = apiInstance.getTransactionsStatuses(transactionHashes);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TransactionRoutesApi#getTransactionsStatuses");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **transactionHashes** | [**TransactionHashes**](TransactionHashes.md)| An array of transaction hashes. |

### Return type

[**List&lt;TransactionStatusDTO&gt;**](TransactionStatusDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

