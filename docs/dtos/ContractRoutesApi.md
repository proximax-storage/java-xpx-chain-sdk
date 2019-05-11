# ContractRoutesApi

All URIs are relative to *http://localhost:3000*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAccountContract**](ContractRoutesApi.md#getAccountContract) | **GET** /account/{publicKey}/contracts | Get contract of account
[**getAccountContracts**](ContractRoutesApi.md#getAccountContracts) | **POST** /account/contracts | Get contracts for an array of publicKeys
[**getContract**](ContractRoutesApi.md#getContract) | **GET** /contract/{accountId} | Get contract of account
[**getContracts**](ContractRoutesApi.md#getContracts) | **POST** /contract | Get contracts for an array of publicKeys or addresses


<a name="getAccountContract"></a>
# **getAccountContract**
> ContractInfoDTO getAccountContract(publicKey)

Get contract of account

Gets the contract for a given publicKey.

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.ContractRoutesApi;


ContractRoutesApi apiInstance = new ContractRoutesApi();
String publicKey = "publicKey_example"; // String | The account identifier.
try {
    ContractInfoDTO result = apiInstance.getAccountContract(publicKey);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractRoutesApi#getAccountContract");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **publicKey** | **String**| The account identifier. |

### Return type

[**ContractInfoDTO**](ContractInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getAccountContracts"></a>
# **getAccountContracts**
> List&lt;ContractInfoDTO&gt; getAccountContracts(publicKeys)

Get contracts for an array of publicKeys

Gets an array of contracts.

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.ContractRoutesApi;


ContractRoutesApi apiInstance = new ContractRoutesApi();
PublicKeys publicKeys = new PublicKeys(); // PublicKeys | An array of public keys.
try {
    List<ContractInfoDTO> result = apiInstance.getAccountContracts(publicKeys);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractRoutesApi#getAccountContracts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **publicKeys** | [**PublicKeys**](PublicKeys.md)| An array of public keys. |

### Return type

[**List&lt;ContractInfoDTO&gt;**](ContractInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getContract"></a>
# **getContract**
> ContractInfoDTO getContract(accountId)

Get contract of account

Gets the contract for a given accountId.

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.ContractRoutesApi;


ContractRoutesApi apiInstance = new ContractRoutesApi();
String accountId = "accountId_example"; // String | The account identifier.
try {
    ContractInfoDTO result = apiInstance.getContract(accountId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractRoutesApi#getContract");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier. |

### Return type

[**ContractInfoDTO**](ContractInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getContracts"></a>
# **getContracts**
> List&lt;ContractInfoDTO&gt; getContracts(addresses)

Get contracts for an array of publicKeys or addresses

Gets an array of contracts.

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.ContractRoutesApi;


ContractRoutesApi apiInstance = new ContractRoutesApi();
Addresses addresses = new Addresses(); // Addresses | An array of addresses.
try {
    List<ContractInfoDTO> result = apiInstance.getContracts(addresses);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractRoutesApi#getContracts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **addresses** | [**Addresses**](Addresses.md)| An array of addresses. |

### Return type

[**List&lt;ContractInfoDTO&gt;**](ContractInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

