/**
 * 
 */
package io.nem.sdk.infrastructure;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.nem.core.crypto.KeyPair;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.AccountInfo;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicInfo;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.Message;
import io.nem.sdk.model.transaction.PlainMessage;
import io.nem.sdk.model.transaction.SecureMessage;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.TransactionAnnounceResponse;
import io.nem.sdk.model.transaction.TransferTransaction;

/**
 * @author tonowie
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2ETransactingTest extends BaseTest {

	private static final NetworkType NETWORK_TYPE = NetworkType.TEST_NET;
	
	private BlockchainHttp blockchainHttp;
	private AccountHttp accountHttp;
	private TransactionHttp transactionHttp;
    private MosaicHttp mosaicHttp;

	private Listener listener;

	@BeforeAll
	void setup() throws ExecutionException, InterruptedException, IOException {
		String nodeUrl = this.getNodeUrl();
		// create HTTP APIs
		transactionHttp = new TransactionHttp(nodeUrl);
		accountHttp = new AccountHttp(nodeUrl);
		blockchainHttp = new BlockchainHttp(nodeUrl);
        mosaicHttp = new MosaicHttp(nodeUrl);
		// prepare listener
		listener = new Listener(nodeUrl);
		listener.open().get();
	}

	@Test
	void sendTransaction() throws InterruptedException, ExecutionException {
		// first retrieve start account with funds
		Account seedAccount = getSeedAccount(NETWORK_TYPE);
		// check that account actually has some funds
		AccountInfo seedAccountInfo = accountHttp.getAccountInfo(seedAccount.getAddress()).toFuture().get();
		List<MosaicId> mosaicIDs = seedAccountInfo.getMosaics().stream().map(Mosaic::getId).collect(Collectors.toList());
		// retrieve list of mosaics
		List<MosaicInfo> mosaics = mosaicHttp.getMosaics(mosaicIDs).toFuture().get();
		assertTrue(!mosaics.isEmpty());
		// TODO check the account balances to make sure we can run the tests
		
		// create transfer transaction with plaintext mesage
		Account targetAccount = new Account(new KeyPair(), NETWORK_TYPE);
		System.out.println("Sending transactions to " + targetAccount);
		// send transaction with plaintext message
		System.out.println(transfer(seedAccount, targetAccount.getAddress(), new Mosaic(mosaicIDs.get(0), BigInteger.valueOf(0l)), new PlainMessage("java SDK plain message test")));
		// send transaction with encrypted message
		SecureMessage secureMessage = SecureMessage.create(seedAccount.getKeyPair().getPrivateKey(), targetAccount.getKeyPair().getPublicKey(), "java SDK secure message");
		System.out.println(transfer(seedAccount, targetAccount.getAddress(), new Mosaic(mosaicIDs.get(0), BigInteger.valueOf(0l)), secureMessage));
        // TODO check the response and wait for inclusion of transaction to the block
//        .subscribe(
//        		(resp) -> System.out.println(resp), 
//        		(err) -> System.out.println(err),
//        		() -> System.out.println("done"));
	}
	
	/**
	 * return transactions as specified by arguments signed by the signer account
	 * 
	 * @param signerAccount account used to sign the transaction
	 * @param target target address for the transfer
	 * @param amount mosaic to transfer
	 * @param message message for the transfer
	 * @return instance of signed transaction which can be then announced to the network
	 */
	private static SignedTransaction signTransfer(Account signerAccount, Address target, Mosaic amount, Message message) {
        TransferTransaction transaction = TransferTransaction.create(
                new Deadline(2, HOURS),
                target,
                Collections.singletonList(amount),
                message,
                NETWORK_TYPE
        );
        return signerAccount.sign(transaction);
	}
	
	private TransactionAnnounceResponse transfer(Account from, Address to, Mosaic mosaic, Message message) throws InterruptedException, ExecutionException {
		SignedTransaction signedTransaction = signTransfer(from, to, mosaic, message);
        return transactionHttp.announce(signedTransaction).toFuture().get();

	}
}
