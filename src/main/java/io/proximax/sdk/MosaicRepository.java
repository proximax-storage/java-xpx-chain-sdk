/*
 * Copyright 2018 NEM
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

package io.proximax.sdk;

import java.util.List;

import io.proximax.sdk.model.mosaic.MosaicInfo;
import io.proximax.sdk.model.mosaic.MosaicNames;
import io.proximax.sdk.model.transaction.UInt64Id;
import io.reactivex.Observable;

/**
 * Mosaic interface repository.
 *
 * @since 1.0
 */
public interface MosaicRepository {

    /**
     * Gets a MosaicInfo for a given mosaicId
     *
     * @param mosaicId BigInteger
     * @return Observable of {@link MosaicInfo}
     */
    Observable<MosaicInfo> getMosaic(UInt64Id mosaicId);

    /**
     * Gets MosaicInfo for different mosaicIds.
     *
     * @param mosaicIds List of BigInteger
     * @return Observable of {@link MosaicInfo} list
     */
    Observable<List<MosaicInfo>> getMosaics(List<UInt64Id> mosaicIds);

    /**
     * Gets list of MosaicName for different mosaicIds.
     *
     * @param mosaicIds List of BigInteger
     * @return Observable of {@link MosaicNames} list
     */
    Observable<List<MosaicNames>> getMosaicNames(List<UInt64Id> mosaicIds);
}
