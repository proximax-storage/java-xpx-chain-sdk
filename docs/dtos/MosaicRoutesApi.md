# MosaicRoutesApi

All URIs are relative to *http://localhost:3000*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getMosaic**](MosaicRoutesApi.md#getMosaic) | **GET** /mosaic/{mosaicId} | Get mosaic information
[**getMosaics**](MosaicRoutesApi.md#getMosaics) | **POST** /mosaic | Get mosaics information for an array of mosaics
[**getMosaicsName**](MosaicRoutesApi.md#getMosaicsName) | **POST** /mosaic/names | Get readable names for a set of mosaics


<a name="getMosaic"></a>
# **getMosaic**
> MosaicInfoDTO getMosaic(mosaicId)

Get mosaic information

Gets the mosaic definition for a given mosaicId.

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.MosaicRoutesApi;


MosaicRoutesApi apiInstance = new MosaicRoutesApi();
String mosaicId = "mosaicId_example"; // String | The mosaic identifier.
try {
    MosaicInfoDTO result = apiInstance.getMosaic(mosaicId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MosaicRoutesApi#getMosaic");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **mosaicId** | **String**| The mosaic identifier. |

### Return type

[**MosaicInfoDTO**](MosaicInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getMosaics"></a>
# **getMosaics**
> List&lt;MosaicInfoDTO&gt; getMosaics(mosaicIds)

Get mosaics information for an array of mosaics

Gets an array of mosaic definition.

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.MosaicRoutesApi;


MosaicRoutesApi apiInstance = new MosaicRoutesApi();
MosaicIds mosaicIds = new MosaicIds(); // MosaicIds | An array of mosaicIds.
try {
    List<MosaicInfoDTO> result = apiInstance.getMosaics(mosaicIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MosaicRoutesApi#getMosaics");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **mosaicIds** | [**MosaicIds**](MosaicIds.md)| An array of mosaicIds. |

### Return type

[**List&lt;MosaicInfoDTO&gt;**](MosaicInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getMosaicsName"></a>
# **getMosaicsName**
> List&lt;MosaicNamesDTO&gt; getMosaicsName(mosaicIds)

Get readable names for a set of mosaics

Returns friendly names for mosaics.

### Example
```java
// Import classes:
//import io.proximax.sdk.api.ApiException;
//import io.proximax.sdk.api.swagger.MosaicRoutesApi;


MosaicRoutesApi apiInstance = new MosaicRoutesApi();
MosaicIds mosaicIds = new MosaicIds(); // MosaicIds | An array of mosaicIds.
try {
    List<MosaicNamesDTO> result = apiInstance.getMosaicsName(mosaicIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MosaicRoutesApi#getMosaicsName");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **mosaicIds** | [**MosaicIds**](MosaicIds.md)| An array of mosaicIds. |

### Return type

[**List&lt;MosaicNamesDTO&gt;**](MosaicNamesDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

