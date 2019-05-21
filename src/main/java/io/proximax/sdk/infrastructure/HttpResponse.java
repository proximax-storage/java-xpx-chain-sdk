/*
 * Copyright 2019 ProximaX
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

import java.io.IOException;

/**
 * generic interface for HTTP responses
 */
public interface HttpResponse {
   /**
    * response code
    * 
    * @return HTTP response code
    */
   int getCode();
   
   /**
    * response status message
    * 
    * @return the string representation of response code
    */
   String getStatusMessage();
   
   /**
    * response body
    * 
    * @return response body as string
    * @throws IOException if body can not be retrieved from the response
    */
   String getBodyString() throws IOException;
}
