# AccountRoutesApi

All URIs are relative to *http://localhost:3000*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAccountInfo**](AccountRoutesApi.md#getAccountInfo) | **GET** /account/{accountId} | Get account information
[**getAccountMultisig**](AccountRoutesApi.md#getAccountMultisig) | **GET** /account/{accountId}/multisig | Get multisig account information
[**getAccountMultisigGraph**](AccountRoutesApi.md#getAccountMultisigGraph) | **GET** /account/{accountId}/multisig/graph | Get multisig account graph information
[**getAccountProperties**](AccountRoutesApi.md#getAccountProperties) | **GET** /account/{accountId}/properties | Get account configurable properties information
[**getAccountPropertiesFromAccounts**](AccountRoutesApi.md#getAccountPropertiesFromAccounts) | **POST** /account/properties | Get account properties for given array of addresses
[**getAccountsInfo**](AccountRoutesApi.md#getAccountsInfo) | **POST** /account | Get accounts information
[**incomingTransactions**](AccountRoutesApi.md#incomingTransactions) | **GET** /account/{publicKey}/transactions/incoming | Get incoming transactions
[**outgoingTransactions**](AccountRoutesApi.md#outgoingTransactions) | **GET** /account/{publicKey}/transactions/outgoing | Get outgoing transactions
[**partialTransactions**](AccountRoutesApi.md#partialTransactions) | **GET** /account/{publicKey}/transactions/partial | Get aggregate bonded transactions information
[**transactions**](AccountRoutesApi.md#transactions) | **GET** /account/{publicKey}/transactions | Get confirmed transactions
[**unconfirmedTransactions**](AccountRoutesApi.md#unconfirmedTransactions) | **GET** /account/{publicKey}/transactions/unconfirmed | Get unconfirmed transactions


<a name="getAccountInfo"></a>
# **getAccountInfo**
> AccountInfoDTO getAccountInfo(accountId)

Get account information

Returns the account information.

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.AccountRoutesApi;


AccountRoutesApi apiInstance = new AccountRoutesApi();
String accountId = "accountId_example"; // String | The public key or address of the account.
try {
    AccountInfoDTO result = apiInstance.getAccountInfo(accountId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountRoutesApi#getAccountInfo");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The public key or address of the account. |

### Return type

[**AccountInfoDTO**](AccountInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getAccountMultisig"></a>
# **getAccountMultisig**
> MultisigAccountInfoDTO getAccountMultisig(accountId)

Get multisig account information

Returns the [multisig account](https://nemtech.github.io/concepts/multisig-account.html) information.

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.AccountRoutesApi;


AccountRoutesApi apiInstance = new AccountRoutesApi();
String accountId = "accountId_example"; // String | The public key or address of the account.
try {
    MultisigAccountInfoDTO result = apiInstance.getAccountMultisig(accountId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountRoutesApi#getAccountMultisig");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The public key or address of the account. |

### Return type

[**MultisigAccountInfoDTO**](MultisigAccountInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getAccountMultisigGraph"></a>
# **getAccountMultisigGraph**
> List&lt;MultisigAccountGraphInfoDTO&gt; getAccountMultisigGraph(accountId)

Get multisig account graph information

Returns the [multisig account](https://nemtech.github.io/concepts/multisig-account.html) graph.

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.AccountRoutesApi;


AccountRoutesApi apiInstance = new AccountRoutesApi();
String accountId = "accountId_example"; // String | The public key or address of the account.
try {
    List<MultisigAccountGraphInfoDTO> result = apiInstance.getAccountMultisigGraph(accountId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountRoutesApi#getAccountMultisigGraph");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The public key or address of the account. |

### Return type

[**List&lt;MultisigAccountGraphInfoDTO&gt;**](MultisigAccountGraphInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getAccountProperties"></a>
# **getAccountProperties**
> AccountPropertiesInfoDTO getAccountProperties(accountId)

Get account configurable properties information

Returns the [configurable properties](https://nemtech.github.io/concepts/account-filter.html) for a given account. 

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.AccountRoutesApi;


AccountRoutesApi apiInstance = new AccountRoutesApi();
String accountId = "accountId_example"; // String | The public key or address of the account.
try {
    AccountPropertiesInfoDTO result = apiInstance.getAccountProperties(accountId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountRoutesApi#getAccountProperties");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The public key or address of the account. |

### Return type

[**AccountPropertiesInfoDTO**](AccountPropertiesInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getAccountPropertiesFromAccounts"></a>
# **getAccountPropertiesFromAccounts**
> List&lt;AccountPropertiesInfoDTO&gt; getAccountPropertiesFromAccounts(addresses)

Get account properties for given array of addresses

Returns the [configurable properties](https://nemtech.github.io/concepts/account-filter.html) for a given array of addresses. 

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.AccountRoutesApi;


AccountRoutesApi apiInstance = new AccountRoutesApi();
Addresses addresses = new Addresses(); // Addresses | An array of addresses.
try {
    List<AccountPropertiesInfoDTO> result = apiInstance.getAccountPropertiesFromAccounts(addresses);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountRoutesApi#getAccountPropertiesFromAccounts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **addresses** | [**Addresses**](Addresses.md)| An array of addresses. |

### Return type

[**List&lt;AccountPropertiesInfoDTO&gt;**](AccountPropertiesInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getAccountsInfo"></a>
# **getAccountsInfo**
> List&lt;AccountInfoDTO&gt; getAccountsInfo(addresses)

Get accounts information

Returns the account information for an array of accounts.

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.AccountRoutesApi;


AccountRoutesApi apiInstance = new AccountRoutesApi();
Addresses addresses = new Addresses(); // Addresses | An array of addresses.
try {
    List<AccountInfoDTO> result = apiInstance.getAccountsInfo(addresses);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountRoutesApi#getAccountsInfo");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **addresses** | [**Addresses**](Addresses.md)| An array of addresses. |

### Return type

[**List&lt;AccountInfoDTO&gt;**](AccountInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="incomingTransactions"></a>
# **incomingTransactions**
> List&lt;Object&gt; incomingTransactions(publicKey, pageSize, id, ordering)

Get incoming transactions

Gets an array of incoming transactions. A transaction is said to be incoming with respect to an account if the account is the recipient of the transaction. 

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.AccountRoutesApi;


AccountRoutesApi apiInstance = new AccountRoutesApi();
String publicKey = "publicKey_example"; // String | The public key of the account.
Integer pageSize = 10; // Integer | The number of transactions to return for each request.
String id = "id_example"; // String | The transaction id up to which transactions are returned. 
String ordering = "-id"; // String | The ordering criteria. * -id: Descending order by id. * id: Ascending order by id. 
try {
    List<Object> result = apiInstance.incomingTransactions(publicKey, pageSize, id, ordering);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountRoutesApi#incomingTransactions");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **publicKey** | **String**| The public key of the account. |
 **pageSize** | **Integer**| The number of transactions to return for each request. | [optional] [default to 10]
 **id** | **String**| The transaction id up to which transactions are returned.  | [optional]
 **ordering** | **String**| The ordering criteria. * -id: Descending order by id. * id: Ascending order by id.  | [optional] [default to -id]

### Return type

**List&lt;Object&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="outgoingTransactions"></a>
# **outgoingTransactions**
> List&lt;Object&gt; outgoingTransactions(publicKey, pageSize, id, ordering)

Get outgoing transactions

Gets an array of outgoing transactions. A transaction is said to be outgoing with respect to an account if the account is the sender of the transaction.

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.AccountRoutesApi;


AccountRoutesApi apiInstance = new AccountRoutesApi();
String publicKey = "publicKey_example"; // String | The public key of the account.
Integer pageSize = 10; // Integer | The number of transactions to return for each request.
String id = "id_example"; // String | The transaction id up to which transactions are returned. 
String ordering = "-id"; // String | The ordering criteria. * -id: Descending order by id. * id: Ascending order by id. 
try {
    List<Object> result = apiInstance.outgoingTransactions(publicKey, pageSize, id, ordering);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountRoutesApi#outgoingTransactions");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **publicKey** | **String**| The public key of the account. |
 **pageSize** | **Integer**| The number of transactions to return for each request. | [optional] [default to 10]
 **id** | **String**| The transaction id up to which transactions are returned.  | [optional]
 **ordering** | **String**| The ordering criteria. * -id: Descending order by id. * id: Ascending order by id.  | [optional] [default to -id]

### Return type

**List&lt;Object&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="partialTransactions"></a>
# **partialTransactions**
> List&lt;Object&gt; partialTransactions(publicKey, pageSize, id, ordering)

Get aggregate bonded transactions information

Gets an array of [aggregate bonded transactions](https://nemtech.github.io/concepts/aggregate-transaction.html) where the account is the sender or requires to cosign the transaction. 

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.AccountRoutesApi;


AccountRoutesApi apiInstance = new AccountRoutesApi();
String publicKey = "publicKey_example"; // String | The public key of the account.
Integer pageSize = 10; // Integer | The number of transactions to return for each request.
String id = "id_example"; // String | The transaction id up to which transactions are returned. 
String ordering = "-id"; // String | The ordering criteria. * -id: Descending order by id. * id: Ascending order by id. 
try {
    List<Object> result = apiInstance.partialTransactions(publicKey, pageSize, id, ordering);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountRoutesApi#partialTransactions");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **publicKey** | **String**| The public key of the account. |
 **pageSize** | **Integer**| The number of transactions to return for each request. | [optional] [default to 10]
 **id** | **String**| The transaction id up to which transactions are returned.  | [optional]
 **ordering** | **String**| The ordering criteria. * -id: Descending order by id. * id: Ascending order by id.  | [optional] [default to -id]

### Return type

**List&lt;Object&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="transactions"></a>
# **transactions**
> List&lt;Object&gt; transactions(publicKey, pageSize, id, ordering)

Get confirmed transactions

Gets an array of transactions for which an account is the sender or receiver.

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.AccountRoutesApi;


AccountRoutesApi apiInstance = new AccountRoutesApi();
String publicKey = "publicKey_example"; // String | The public key of the account.
Integer pageSize = 10; // Integer | The number of transactions to return for each request.
String id = "id_example"; // String | The transaction id up to which transactions are returned. 
String ordering = "-id"; // String | The ordering criteria. * -id: Descending order by id. * id: Ascending order by id. 
try {
    List<Object> result = apiInstance.transactions(publicKey, pageSize, id, ordering);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountRoutesApi#transactions");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **publicKey** | **String**| The public key of the account. |
 **pageSize** | **Integer**| The number of transactions to return for each request. | [optional] [default to 10]
 **id** | **String**| The transaction id up to which transactions are returned.  | [optional]
 **ordering** | **String**| The ordering criteria. * -id: Descending order by id. * id: Ascending order by id.  | [optional] [default to -id]

### Return type

**List&lt;Object&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="unconfirmedTransactions"></a>
# **unconfirmedTransactions**
> List&lt;Object&gt; unconfirmedTransactions(publicKey, pageSize, id, ordering)

Get unconfirmed transactions

Gets the array of transactions not included in a block where an account is the sender or receiver. 

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.AccountRoutesApi;


AccountRoutesApi apiInstance = new AccountRoutesApi();
String publicKey = "publicKey_example"; // String | The public key of the account.
Integer pageSize = 10; // Integer | The number of transactions to return for each request.
String id = "id_example"; // String | The transaction id up to which transactions are returned. 
String ordering = "-id"; // String | The ordering criteria. * -id: Descending order by id. * id: Ascending order by id. 
try {
    List<Object> result = apiInstance.unconfirmedTransactions(publicKey, pageSize, id, ordering);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountRoutesApi#unconfirmedTransactions");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **publicKey** | **String**| The public key of the account. |
 **pageSize** | **Integer**| The number of transactions to return for each request. | [optional] [default to 10]
 **id** | **String**| The transaction id up to which transactions are returned.  | [optional]
 **ordering** | **String**| The ordering criteria. * -id: Descending order by id. * id: Ascending order by id.  | [optional] [default to -id]

### Return type

**List&lt;Object&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

