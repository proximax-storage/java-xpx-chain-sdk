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

import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.AccountInfo;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicInfo;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.PlainMessage;
import io.nem.sdk.model.transaction.SignedTransaction;
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
		
		// create transfer transaction for some hardcoded account
		Account targetAccount = new Account("787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d", NETWORK_TYPE);
        TransferTransaction transferTransaction = TransferTransaction.create(
                new Deadline(2, HOURS),
                targetAccount.getAddress(),
                Collections.singletonList(
                        new Mosaic(mosaicIDs.get(0), BigInteger.valueOf(0l))
                ),
                new PlainMessage("java sdk test"),
                NETWORK_TYPE
        );
        SignedTransaction signedTransaction = seedAccount.sign(transferTransaction);
        System.out.println(transactionHttp.announce(signedTransaction).toFuture().get());
        // TODO check the response and wait for inclusion of transaction to the block
//        .subscribe(
//        		(resp) -> System.out.println(resp), 
//        		(err) -> System.out.println(err),
//        		() -> System.out.println("done"));
	}
}
