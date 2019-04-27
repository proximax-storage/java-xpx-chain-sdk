/**
 * 
 */
package io.nem.sdk.infrastructure;

import java.io.IOException;
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

/**
 * @author tonowie
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2ETransactingTest extends BaseTest {

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
		Account startAccount = getSeedAccount(NetworkType.TEST_NET);
		// check that account actually has some funds
		AccountInfo startAccountInfo = accountHttp.getAccountInfo(startAccount.getAddress()).toFuture().get();
		List<MosaicId> mosaicIDs = startAccountInfo.getMosaics().stream().map(Mosaic::getId).collect(Collectors.toList());
		// just print the mosaics owned by the account. no expectations for the test apart from calls not failing
		mosaicHttp.getMosaics(mosaicIDs).toFuture().get().stream().forEachOrdered(System.out::println);
	}
	
}
