/*
 * Copyright 2022 ProximaX
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
package io.proximax.sdk.model.mosaic;

import java.math.BigInteger;
import static io.proximax.sdk.utils.dto.UInt64Utils.toBigInt;

import io.proximax.sdk.gen.model.MosaicLevyInfoDTO;
import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.network.NetworkType;
import io.proximax.sdk.model.transaction.Recipient;

public class MosaicLevyInfo {
    private final MosaicLevyType type;
    private final Recipient recipient;
    private final MosaicId mosaicId;
    private final BigInteger fee;

    /**
     * Constructor
     * 
     * @param type      type of the mosaic levy
     * @param recipient Recipient
     * @param mosaicId  id of the mosaic
     * @param fee       fee of the mosaic levy
     */
    public MosaicLevyInfo(MosaicLevyType type, Recipient recipient, MosaicId mosaicId, BigInteger fee) {
        this.type = type;
        this.recipient = recipient;
        this.mosaicId = mosaicId;
        this.fee = fee;
    }
    
    /**
     * Returns Recipient of mosaic levy.
     *
     * @return {@link Recipient} of mosaic levy
     */
    public Recipient getRecipient() {
        return recipient;
    }

    /**
     * Returns Mosaic Id of mosaic levy.
     *
     * @return {@link MosaicId} of mosaic levy
     * 
     */
    public MosaicId getMosaicId() {
        return mosaicId;
    }

    /**
     * Returns Fee of mosaic levy.
     *
     * @return Mosaic levy fee
     * 
     */
    public BigInteger getFee() {
        return fee;
    }

    /**
     * Returns type of mosaic levy
     *
     * @return {@link MosaicLevyType}
     * 
     */
    public MosaicLevyType getType() {
        return type;
    }
    /**
     * create mosaic levy instance from the DTO
     * 
     * @param dto         Mosaic levy info DTO
     * @param networkType Network type
     * @return mosaic levy info
     */
    public static MosaicLevyInfo fromDto(MosaicLevyInfoDTO dto, NetworkType networkType) {
        return new MosaicLevyInfo(
                MosaicLevyType.rawValueOf(dto.getMosaicLevyType().getValue()),
                new Recipient(Address.createFromEncoded(dto.getRecipient())),
                new MosaicId(toBigInt(dto.getMosaicId())),
                toBigInt(dto.getFee()));
    }

  
}
