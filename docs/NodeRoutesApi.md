# NodeRoutesApi

All URIs are relative to *http://localhost:3000*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getNodeInfo**](NodeRoutesApi.md#getNodeInfo) | **GET** /node/info | Get the node information
[**getNodeTime**](NodeRoutesApi.md#getNodeTime) | **GET** /node/time | Get the node time


<a name="getNodeInfo"></a>
# **getNodeInfo**
> NodeInfoDTO getNodeInfo()

Get the node information

Supplies additional information about the application running on a node. 

### Example
```java
// Import classes:
//import io.nem.sdk.api.ApiException;
//import io.nem.sdk.api.swagger.NodeRoutesApi;


NodeRoutesApi apiInstance = new NodeRoutesApi();
try {
    NodeInfoDTO result = apiInstance.getNodeInfo();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NodeRoutesApi#getNodeInfo");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**NodeInfoDTO**](NodeInfoDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getNodeTime"></a>
# **getNodeTime**
> NodeTimeDTO getNodeTime()

Get the node time

Supplies additional information about the application running on a node.

### Example
```java
// Import classes:
//import io.nem.sdk.api.ApiException;
//import io.nem.sdk.api.swagger.NodeRoutesApi;


NodeRoutesApi apiInstance = new NodeRoutesApi();
try {
    NodeTimeDTO result = apiInstance.getNodeTime();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NodeRoutesApi#getNodeTime");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**NodeTimeDTO**](NodeTimeDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

