/**
 * 
 */
package io.nem.sdk;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nem.core.crypto.KeyPair;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.mosaic.Mosaic;
import io.nem.sdk.model.mosaic.XPX;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.Message;
import io.nem.sdk.model.transaction.PlainMessage;
import io.nem.sdk.model.transaction.SecureMessage;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.TransferTransaction;

/**
 * @author tonowie
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2ETransferTest extends E2EBaseTest {
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(E2ETransferTest.class);

   private final Account simpleAccount = new Account(new KeyPair(), NETWORK_TYPE);

   @BeforeAll
   void addListener() {
      logger.info("Sending transactions to {}", simpleAccount);
      listener.status(simpleAccount.getAddress()).doOnNext(err -> logger.info("Error on recipient: {}", err))
            .doOnError(exception -> logger.error("Failure on recipient: {}", exception))
            .doOnComplete(() -> logger.info("done with recipient {}", simpleAccount));
   }

   @AfterAll
   void verifyResults() throws InterruptedException, ExecutionException {
      // check that target account has expected number of incoming transactions
      int transactions = accountHttp.incomingTransactions(simpleAccount.getPublicAccount()).toFuture().get().size();
      assertEquals(2, transactions);
      // return the xem
      transfer(simpleAccount,
            seedAccount.getAddress(),
            XPX.createAbsolute(BigInteger.valueOf(2)),
            new PlainMessage("money back guarantee"));
   }

   @Test
   void sendPlaintextMessage() {
      // send transaction with plain message
      transfer(seedAccount,
            simpleAccount.getAddress(),
            XPX.createAbsolute(BigInteger.valueOf(1)),
            new PlainMessage("java SDK plain message test"));
   }

   @Test
   void sendSecureMessage() {
      // send transaction with encrypted message
      SecureMessage secureMessage = SecureMessage.create(seedAccount.getKeyPair().getPrivateKey(),
            simpleAccount.getKeyPair().getPublicKey(),
            "java SDK secure message");
      transfer(seedAccount, simpleAccount.getAddress(), XPX.createAbsolute(BigInteger.valueOf(1)), secureMessage);
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
   private static SignedTransaction signTransfer(Account signerAccount, Address target, Mosaic amount,
         Message message) {
      TransferTransaction transaction = TransferTransaction
            .create(new Deadline(2, HOURS), target, Collections.singletonList(amount), message, NETWORK_TYPE);
      return signerAccount.sign(transaction);
   }

   /**
    * create transaction and announce it
    * 
    * @param from account used to sign the transaction
    * @param to target address for the transfer
    * @param mosaic mosaic to transfer
    * @param message message for the transfer
    * @return transaction announce response message
    * @throws InterruptedException
    * @throws ExecutionException
    */
   private void transfer(Account from, Address to, Mosaic mosaic, Message message) {
      SignedTransaction signedTransaction = signTransfer(from, to, mosaic, message);
      logger.info("Transfer announced. {}", transactionHttp.announce(signedTransaction).blockingFirst());
      logger.info("Transfer done. {}", listener.confirmed(from.getAddress()).blockingFirst());

   }

}
