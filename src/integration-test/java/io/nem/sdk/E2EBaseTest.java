/**
 * 
 */
package io.nem.sdk;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nem.sdk.infrastructure.AccountHttp;
import io.nem.sdk.infrastructure.BlockchainHttp;
import io.nem.sdk.infrastructure.Listener;
import io.nem.sdk.infrastructure.MosaicHttp;
import io.nem.sdk.infrastructure.TransactionHttp;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.AccountInfo;
import io.nem.sdk.model.blockchain.BlockInfo;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicInfo;

/**
 * @author tonowie
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2EBaseTest extends BaseTest {
	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(E2EBaseTest.class);
	
	/** network type for IT tests */
	protected static final NetworkType NETWORK_TYPE = NetworkType.TEST_NET;
	
	protected BlockchainHttp blockchainHttp;
	protected AccountHttp accountHttp;
	protected TransactionHttp transactionHttp;
	protected MosaicHttp mosaicHttp;

	protected Listener listener;
	
	protected Account seedAccount;
	protected MosaicInfo mosaic;
	
	@BeforeAll
	void setup() throws ExecutionException, InterruptedException, IOException {
		logger.info("Preparing for tests");
		String nodeUrl = this.getNodeUrl();
		// create HTTP APIs
		transactionHttp = new TransactionHttp(nodeUrl);
		accountHttp = new AccountHttp(nodeUrl);
		blockchainHttp = new BlockchainHttp(nodeUrl);
        mosaicHttp = new MosaicHttp(nodeUrl);
		// prepare listener
		listener = new Listener(nodeUrl);
		listener.open().get();
		// retrieve the seed account which has for tests
		seedAccount = getSeedAccount(NETWORK_TYPE);
		// get the mosaic that will be used here
		mosaic = getMosaic();
	}
	
	@AfterAll
	void cleanup() {
		logger.info("Cleaning up");
        listener.close();
	}

	/**
	 * retrieve mosaic from seed account that will be used to transfer funds
	 * 
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private MosaicInfo getMosaic() throws InterruptedException, ExecutionException {
		// check that account actually has some funds
		AccountInfo seedAccountInfo = accountHttp.getAccountInfo(seedAccount.getAddress()).toFuture().get();
		List<MosaicId> seedMosaicIDs = seedAccountInfo.getMosaics().stream().map(Mosaic::getId).collect(Collectors.toList());
		List<MosaicInfo> mosaics = mosaicHttp.getMosaics(seedMosaicIDs).toFuture().get();
		logger.info("Seed account has following mosaics: {}", mosaics);
		// TODO pick the mosaic to be used here
		return mosaics.get(0);
	}
	
	/**
	 * wait for next block and return the block info
	 * 
	 * @return block info
	 * @throws InterruptedException when wait for block is interrupted
	 * @throws ExecutionException when retrieval of block info fails
	 */
	protected BlockInfo waitForBlock(int numberOfBlocks) throws InterruptedException, ExecutionException {
		logger.info("Waiting for {} blocks", numberOfBlocks);
        // wait for new block so we know all is on the blockchain
        return listener.newBlock()
        		.take(numberOfBlocks)
        		.doOnNext((block) -> logger.info("Block created with height {}", block.getHeight()))
        		.blockingLast();
	}
}
