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
import java.util.OptionalInt;


import io.proximax.sdk.model.transaction.Recipient;

public class MosaicLevy {
    private static final int defaultMosaicLevyOnePercentValue = 100000;
    private MosaicLevyType type;
    private Recipient recipient;
    private MosaicId mosaicId;
    private BigInteger fee;
    
    /**
     * create new instance with Mosaic Levy
     * 
     * @param type      type of the mosaic levy
     * @param recipient Recipient
     * @param mosaicId  id of the mosaic
     * @param fee       fee of the mosaic levy
     */
    public MosaicLevy(MosaicLevyType type, Recipient recipient, MosaicId mosaicId, BigInteger fee) {
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
     * Create mosaic levy with fee that specified as multiple of the percentile
      of the quantity that is transferred
     * 
     * @param recipient             Recipient
     * @param mosaicId              id of the mosaic
     * @param percentage            percentage 
     * @param mosaicOnePercentValue Mosaic value per percent (optional)
     */
    public static MosaicLevy createWithPercentageFee(Recipient recipient, MosaicId mosaicId, float percentage, OptionalInt mosaicOnePercentValue) {
        BigInteger fee = createLevyFeePercentile(percentage, mosaicOnePercentValue);
        return new MosaicLevy(MosaicLevyType.LEVYPERCENTILEFEE, recipient, mosaicId, fee);
    }

    /**
     * create mosaic levy with fee which specified as absolute quantity
     * 
     * @param recipient             Recipient of the levy
     * @param mosaicId              ID of the mosaic
     * @param amount                Amount of special fee
     * @return MosaicLevy           MosaicLevy
     */
    public static MosaicLevy createWithAbsoluteFee(Recipient recipient, MosaicId mosaicId,
            int amount) {
        return new MosaicLevy(MosaicLevyType.LEVYABSOLUTEFEE, recipient, mosaicId, 
        BigInteger.valueOf(amount));
    }

    /**
     * calculate fee based on percentage and mosaic percent value
     * 
     * @param percentage
     * @param mosaicOnePercentValue
     */
    private static BigInteger createLevyFeePercentile(float percentage, OptionalInt mosaicOnePercentValue) {
        if (mosaicOnePercentValue.isPresent()) {
            return BigInteger.valueOf((int) percentage * mosaicOnePercentValue.getAsInt());
        } else {
            return BigInteger.valueOf((int) percentage * defaultMosaicLevyOnePercentValue);
        }
    }
    
     @Override
     public String toString() {
         return "MosaicLevy [type=" + type + ", recipient=" + recipient + ", mosaicId=" + mosaicId + ", fee=" + fee + "]";
     }
    
  
}
