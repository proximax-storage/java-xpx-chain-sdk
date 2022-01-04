/*
 * Copyright 2022 ProximaX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.proximax.sdk;

import java.util.List;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.exchange.*;
import io.proximax.sdk.model.mosaic.MosaicId;
import io.reactivex.Observable;

public interface ExchangeRepository {

    /**
     * <p>
     * Get offers list
     * </p>
     * <p>
     * GET '/exchange/mosaics'
     * </p>
     * 
     * @return observable {@link ExchangeMosaicsList}
     */
    Observable<List<ExchangeMosaicsList>> getOfferList();
    
    /**
     * <p>
     * Get account exchange
     * </p>
     * <p>
     * GET '/account/{address}/exchange'
     * </p>
     * 
     * @return observable {@link AccountExchanges}
     */
    Observable<AccountExchanges> getAccountExchanges(Address address);

    /**
     * <p>
     * Get exchange offers
     * </p>
     * <p>
     * GET '/exchange/{offerType}/{mosaicId}'
     * </p>
     * 
     * @return observable {@link MosaicExchange}
     */
    Observable<List<MosaicExchange>> getExchangeOffers(String offerType, MosaicId mosaicId);
}
