# NetworkRoutesApi

All URIs are relative to *http://localhost:3000*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getNetworkType**](NetworkRoutesApi.md#getNetworkType) | **GET** /network | Get the current network type of the chain


<a name="getNetworkType"></a>
# **getNetworkType**
> NetworkTypeDTO getNetworkType()

Get the current network type of the chain

Returns the current network type.

### Example
```java
// Import classes:
//import io.nem.sdk.api.ApiException;
//import io.nem.sdk.api.swagger.NetworkRoutesApi;


NetworkRoutesApi apiInstance = new NetworkRoutesApi();
try {
    NetworkTypeDTO result = apiInstance.getNetworkType();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetworkRoutesApi#getNetworkType");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**NetworkTypeDTO**](NetworkTypeDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

