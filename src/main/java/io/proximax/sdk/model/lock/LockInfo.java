package io.proximax.sdk.model.lock;

import java.math.BigInteger;

import io.proximax.sdk.model.account.Address;
import io.proximax.sdk.model.transaction.UInt64Id;

public class LockInfo {
    private final String account;
    private final Address accountAddress;
    private final UInt64Id mosaicId;
    private final BigInteger amount;
    private final BigInteger height;

    public LockInfo(
            String account, Address accountAddress, UInt64Id mosaicId, BigInteger amount, BigInteger height) {
        this.account = account;
        this.accountAddress = accountAddress;
        this.mosaicId = mosaicId;
        this.amount = amount;
        this.height = height;
    }

    /**
     * @return account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @return accountAddress
     */
    public Address getAccountAddress() {
        return accountAddress;
    }

    /**
     * @return mosaicId
     */
    public UInt64Id getMosaicId() {
        return mosaicId;
    }

    /**
     * @return amount
     */
    public BigInteger getAmount() {
        return amount;
    }

    /**
     * @return height
     */
    public BigInteger getHeight() {
        return height;
    }

   
}
