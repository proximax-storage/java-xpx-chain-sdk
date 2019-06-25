# NamespaceRoutesApi

All URIs are relative to *http://localhost:3000*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getNamespace**](NamespaceRoutesApi.md#getNamespace) | **GET** /namespace/{namespaceId} | Get namespace information
[**getNamespacesFromAccount**](NamespaceRoutesApi.md#getNamespacesFromAccount) | **GET** /account/{accountId}/namespaces | Get namespaces owned by an account
[**getNamespacesFromAccounts**](NamespaceRoutesApi.md#getNamespacesFromAccounts) | **POST** /account/namespaces | Get namespaces for given array of addresses
[**getNamespacesNames**](NamespaceRoutesApi.md#getNamespacesNames) | **POST** /namespace/names | Get readable names for a set of namespaces


<a name="getNamespace"></a>
# **getNamespace**
> NamespaceInfoDTO getNamespace(namespaceId)

Get namespace information

Gets the namespace for a given namespaceId.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.NamespaceRoutesApi;


NamespaceRoutesApi apiInstance = new NamespaceRoutesApi();
String namespaceId = "namespaceId_example"; // String | The namespace identifier.
try {
    NamespaceInfoDTO result = apiInstance.getNamespace(namespaceId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NamespaceRoutesApi#getNamespace");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **namespaceId** | **String**| The namespace identifier. |

### Return type

[**NamespaceInfoDTO**](NamespaceInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getNamespacesFromAccount"></a>
# **getNamespacesFromAccount**
> List&lt;NamespaceInfoDTO&gt; getNamespacesFromAccount(accountId, pageSize, id)

Get namespaces owned by an account

Gets an array of namespaces for a given account address.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.NamespaceRoutesApi;


NamespaceRoutesApi apiInstance = new NamespaceRoutesApi();
String accountId = "accountId_example"; // String | The address or public key of the account.
Integer pageSize = 56; // Integer | The number of namespaces to return.
String id = "id_example"; // String | The namespace id up to which namespace objects are returned.
try {
    List<NamespaceInfoDTO> result = apiInstance.getNamespacesFromAccount(accountId, pageSize, id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NamespaceRoutesApi#getNamespacesFromAccount");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The address or public key of the account. |
 **pageSize** | **Integer**| The number of namespaces to return. | [optional]
 **id** | **String**| The namespace id up to which namespace objects are returned. | [optional]

### Return type

[**List&lt;NamespaceInfoDTO&gt;**](NamespaceInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getNamespacesFromAccounts"></a>
# **getNamespacesFromAccounts**
> List&lt;NamespaceInfoDTO&gt; getNamespacesFromAccounts(addresses, pageSize, id)

Get namespaces for given array of addresses

Gets namespaces for a given array of addresses.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.NamespaceRoutesApi;


NamespaceRoutesApi apiInstance = new NamespaceRoutesApi();
Addresses addresses = new Addresses(); // Addresses | An array of addresses.
Integer pageSize = 56; // Integer | The number of namespaces to return.
String id = "id_example"; // String | The namespace id up to which namespace objects are returned.
try {
    List<NamespaceInfoDTO> result = apiInstance.getNamespacesFromAccounts(addresses, pageSize, id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NamespaceRoutesApi#getNamespacesFromAccounts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **addresses** | [**Addresses**](Addresses.md)| An array of addresses. |
 **pageSize** | **Integer**| The number of namespaces to return. | [optional]
 **id** | **String**| The namespace id up to which namespace objects are returned. | [optional]

### Return type

[**List&lt;NamespaceInfoDTO&gt;**](NamespaceInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getNamespacesNames"></a>
# **getNamespacesNames**
> List&lt;NamespaceNameDTO&gt; getNamespacesNames(namespaceIds)

Get readable names for a set of namespaces

Returns friendly names for mosaics.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.NamespaceRoutesApi;


NamespaceRoutesApi apiInstance = new NamespaceRoutesApi();
NamespaceIds namespaceIds = new NamespaceIds(); // NamespaceIds | An array of namespaceIds.
try {
    List<NamespaceNameDTO> result = apiInstance.getNamespacesNames(namespaceIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NamespaceRoutesApi#getNamespacesNames");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **namespaceIds** | [**NamespaceIds**](NamespaceIds.md)| An array of namespaceIds. |

### Return type

[**List&lt;NamespaceNameDTO&gt;**](NamespaceNameDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

