# MetadataRoutesApi

All URIs are relative to *http://localhost:3000*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAccountMetadata**](MetadataRoutesApi.md#getAccountMetadata) | **GET** /account/{accountId}/metadata | Get metadata of account
[**getMetadata**](MetadataRoutesApi.md#getMetadata) | **GET** /metadata/{metadataId} | Get metadata of namespace/mosaic/account
[**getMetadatas**](MetadataRoutesApi.md#getMetadatas) | **POST** /metadata | Get metadatas(namespace/mosaic/account) for an array of metadataids
[**getMosaicMetadata**](MetadataRoutesApi.md#getMosaicMetadata) | **GET** /mosaic/{mosaicId}/metadata | Get metadata of mosaic
[**getNamespaceMetadata**](MetadataRoutesApi.md#getNamespaceMetadata) | **GET** /namespace/{namespaceId}/metadata | Get metadata of namespace


<a name="getAccountMetadata"></a>
# **getAccountMetadata**
> AddressMetadataInfoDTO getAccountMetadata(accountId)

Get metadata of account

Gets the metadata for a given accountId.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.MetadataRoutesApi;


MetadataRoutesApi apiInstance = new MetadataRoutesApi();
String accountId = "accountId_example"; // String | The account identifier.
try {
    AddressMetadataInfoDTO result = apiInstance.getAccountMetadata(accountId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataRoutesApi#getAccountMetadata");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier. |

### Return type

[**AddressMetadataInfoDTO**](AddressMetadataInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getMetadata"></a>
# **getMetadata**
> NamespaceMetadataInfoDTO getMetadata(metadataId)

Get metadata of namespace/mosaic/account

Gets the metadata(AccountMetadataIndo, MosaicMetadataInfo or NamespaceMetadataInfo) for a given metadataId.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.MetadataRoutesApi;


MetadataRoutesApi apiInstance = new MetadataRoutesApi();
String metadataId = "metadataId_example"; // String | The metadata identifier.
try {
    NamespaceMetadataInfoDTO result = apiInstance.getMetadata(metadataId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataRoutesApi#getMetadata");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **metadataId** | **String**| The metadata identifier. |

### Return type

[**NamespaceMetadataInfoDTO**](NamespaceMetadataInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getMetadatas"></a>
# **getMetadatas**
> List&lt;AddressMetadataInfoDTO&gt; getMetadatas(metadataIds)

Get metadatas(namespace/mosaic/account) for an array of metadataids

Gets an array of metadata.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.MetadataRoutesApi;


MetadataRoutesApi apiInstance = new MetadataRoutesApi();
MetadataIds metadataIds = new MetadataIds(); // MetadataIds | An array of metadataIds.
try {
    List<AddressMetadataInfoDTO> result = apiInstance.getMetadatas(metadataIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataRoutesApi#getMetadatas");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **metadataIds** | [**MetadataIds**](MetadataIds.md)| An array of metadataIds. |

### Return type

[**List&lt;AddressMetadataInfoDTO&gt;**](AddressMetadataInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getMosaicMetadata"></a>
# **getMosaicMetadata**
> MosaicMetadataInfoDTO getMosaicMetadata(mosaicId)

Get metadata of mosaic

Gets the metadata for a given mosaicId.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.MetadataRoutesApi;


MetadataRoutesApi apiInstance = new MetadataRoutesApi();
String mosaicId = "mosaicId_example"; // String | The mosaic identifier.
try {
    MosaicMetadataInfoDTO result = apiInstance.getMosaicMetadata(mosaicId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataRoutesApi#getMosaicMetadata");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **mosaicId** | **String**| The mosaic identifier. |

### Return type

[**MosaicMetadataInfoDTO**](MosaicMetadataInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getNamespaceMetadata"></a>
# **getNamespaceMetadata**
> NamespaceMetadataInfoDTO getNamespaceMetadata(namespaceId)

Get metadata of namespace

Gets the metadata for a given namespaceId.

### Example
```java
// Import classes:
//import io.proximax.sdk.gen.ApiException;
//import io.proximax.sdk.gen.api.MetadataRoutesApi;


MetadataRoutesApi apiInstance = new MetadataRoutesApi();
String namespaceId = "namespaceId_example"; // String | The namespace identifier.
try {
    NamespaceMetadataInfoDTO result = apiInstance.getNamespaceMetadata(namespaceId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataRoutesApi#getNamespaceMetadata");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **namespaceId** | **String**| The namespace identifier. |

### Return type

[**NamespaceMetadataInfoDTO**](NamespaceMetadataInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

