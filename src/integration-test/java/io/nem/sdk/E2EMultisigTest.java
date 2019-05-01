/**
 * 
 */
package io.nem.sdk;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nem.core.crypto.KeyPair;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.MultisigAccountInfo;
import io.nem.sdk.model.mosaic.XPX;
import io.nem.sdk.model.transaction.*;
import io.reactivex.Observable;

/**
 * Multisig integration tests
 * 
 * @author tonowie
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class E2EMultisigTest extends E2EBaseTest {
   /** slf4j logger */
   private static final Logger logger = LoggerFactory.getLogger(E2EMultisigTest.class);

   private final Account multisigAccount = new Account(new KeyPair(), NETWORK_TYPE);
   private final Account cosig1 = new Account(new KeyPair(), NETWORK_TYPE);
   private final Account cosig2 = new Account(new KeyPair(), NETWORK_TYPE);
   private final Account cosig3 = new Account(new KeyPair(), NETWORK_TYPE);

   private Observable<Transaction> multisigConfirmations;
   private Observable<Transaction> cosig1Confirmations;
   private Observable<Transaction> cosig2Confirmations;

   @AfterAll
   void verifyResults() throws InterruptedException, ExecutionException {
      // send back 10XPX to the seed account from cosig1
      sendSomeCash(cosig1, seedAccount.getAddress(), 10);
   }

   @BeforeAll
   void prepareForTest() throws InterruptedException, ExecutionException {
      listener.status(multisigAccount.getAddress()).doOnNext(err -> logger.error("Some operation failed: {}", err))
            .doOnError(t -> logger.error("exception thrown", t));
      listener.status(cosig1.getAddress()).doOnNext(err -> logger.error("Some operation failed: {}", err))
            .doOnError(t -> logger.error("exception thrown", t));
      listener.status(cosig2.getAddress()).doOnNext(err -> logger.error("Some operation failed: {}", err))
            .doOnError(t -> logger.error("exception thrown", t));
      listener.status(cosig3.getAddress()).doOnNext(err -> logger.error("Some operation failed: {}", err))
            .doOnError(t -> logger.error("exception thrown", t));
      multisigConfirmations = listener.confirmed(multisigAccount.getAddress());
      cosig1Confirmations = listener.confirmed(cosig1.getAddress());
      cosig2Confirmations = listener.confirmed(cosig2.getAddress());
   }


   @Test
   void test00GetFunds() {
      // send 1XPX to the to-be-multisig account so we can test transfers later on
      sendSomeCash(seedAccount, multisigAccount.getAddress(), 1);
      // give cosig1 10XPX so he can lock funds for aggregate transactions
      sendSomeCash(seedAccount, cosig1.getAddress(), 10);
   }
   
   @Test
   void test01CreateMultisig() throws InterruptedException, ExecutionException {
      logger.info("Going to make account multisig: {}", multisigAccount);
      // change the account to multisig with 1 of2 cosignatories
      ModifyMultisigAccountTransaction changeToMultisig = ModifyMultisigAccountTransaction.create(
            new Deadline(2, HOURS),
            1,
            1,
            Arrays.asList(
                  new MultisigCosignatoryModification(MultisigCosignatoryModificationType.ADD,
                        cosig1.getPublicAccount()),
                  new MultisigCosignatoryModification(MultisigCosignatoryModificationType.ADD,
                        cosig2.getPublicAccount())),
            NETWORK_TYPE);
      SignedTransaction signedChangeToMultisig = multisigAccount.sign(changeToMultisig);
      // announce the transaction
      logger.info("Sent request: {}", transactionHttp.announce(signedChangeToMultisig).toFuture().get());
      // verify that account is multisig
      logger.info("request confirmed: {}", multisigConfirmations.blockingFirst());
      testMultisigAccount(true, 1, 1, 2);
   }
   
   @Test
   void test01TransferFromMultisig1Of2() {
      // prepare transfer to the seed account
      TransferTransaction transfer = TransferTransaction.create(new Deadline(2, HOURS),
            seedAccount.getAddress(),
            Collections.singletonList(XPX.createAbsolute(BigInteger.valueOf(1))),
            PlainMessage.Empty,
            NETWORK_TYPE);
      // add the modification to the aggregate transaction
      AggregateTransaction aggregateTransaction = AggregateTransaction.createComplete(Deadline.create(2, HOURS),
            Arrays.asList(transfer.toAggregate(multisigAccount.getPublicAccount())),
            NETWORK_TYPE);
      // sign the aggregate transaction
      SignedTransaction signedTransaction = cosig1.sign(aggregateTransaction);
      // announce the transfer
      logger.info("Announced the transfer from multisig: {}",
            transactionHttp.announce(signedTransaction).blockingFirst());
      logger.info("request confirmed: {}", cosig1Confirmations.blockingFirst());
   }
   
   @Test
   void test01TransferFromMultisig1Of2Bonded() {
      // prepare transfer to the seed account
      TransferTransaction transfer = TransferTransaction.create(new Deadline(2, HOURS),
            seedAccount.getAddress(),
            Collections.singletonList(XPX.createAbsolute(BigInteger.valueOf(1))),
            PlainMessage.Empty,
            NETWORK_TYPE);
      // add the modification to the aggregate transaction. has to be bonded because we are going to test the lock
      AggregateTransaction aggregateTransaction = AggregateTransaction.createBonded(Deadline.create(2, HOURS),
            Arrays.asList(transfer.toAggregate(multisigAccount.getPublicAccount())),
            NETWORK_TYPE);
      // sign the aggregate bonded transaction
      SignedTransaction signedTransaction = cosig1.sign(aggregateTransaction);
      // lock 10 of XPX (required to prevent spamming)
      LockFundsTransaction lockFundsTransaction = LockFundsTransaction.create(Deadline.create(2, HOURS),
            XPX.createRelative(BigInteger.valueOf(10)),
            BigInteger.valueOf(480),
            signedTransaction,
            NETWORK_TYPE);
      // sign the fund lock
      SignedTransaction lockFundsTransactionSigned = cosig1.sign(lockFundsTransaction);
      // announce the lock transaction
      logger.info("Sent request to lock funds: {}",
            transactionHttp.announce(lockFundsTransactionSigned).blockingFirst());
      logger.info("request confirmed: {}", listener.aggregateBondedAdded(cosig1.getAddress()).blockingFirst());
      // announce the multisig change as aggregate bounded
      logger.info("Announced the multisig change: {}",
            transactionHttp.announceAggregateBonded(signedTransaction).blockingFirst());
      logger.info("request confirmed: {}", cosig1Confirmations.blockingFirst());

   }
   
   @Test
   void test02IncreaseMinApproval() throws InterruptedException, ExecutionException {
      logger.info("Going to make the account 2 of 2");
      // modify account to be 2 of 2
      ModifyMultisigAccountTransaction changeTo2of2 = ModifyMultisigAccountTransaction.create(new Deadline(2, HOURS),
            1, // add one to the minApproval to change it to 2
            0,
            Arrays.asList(),
            NETWORK_TYPE);
      AggregateTransaction aggregateTransaction = AggregateTransaction.createComplete(new Deadline(2, HOURS),
            Arrays.asList(changeTo2of2.toAggregate(multisigAccount.getPublicAccount())),
            NETWORK_TYPE);
      SignedTransaction signedChangeTo2of2 = cosig1.sign(aggregateTransaction);
      logger.info("Sent request: {}", transactionHttp.announce(signedChangeTo2of2).toFuture().get());
      // verify that min approvals is set to 2
      logger.info("request confirmed: {}", cosig1Confirmations.blockingFirst());
      testMultisigAccount(true, 2, 1, 2);
   }

   @Test
   void test03AddCosignatory() throws InterruptedException, ExecutionException {
      logger.info("Going to add third cosignatory");
      // create multisig modification
      ModifyMultisigAccountTransaction addCosig3 = ModifyMultisigAccountTransaction.create(Deadline.create(2, HOURS),
            0,
            0,
            Arrays.asList(new MultisigCosignatoryModification(MultisigCosignatoryModificationType.ADD,
                  cosig3.getPublicAccount())),
            NETWORK_TYPE);
      // add the modification to the aggregate bonded transaction
      AggregateTransaction aggregateTransaction = AggregateTransaction.createBonded(Deadline.create(2, HOURS),
            Arrays.asList(addCosig3.toAggregate(multisigAccount.getPublicAccount())),
            NETWORK_TYPE);
      // sign the aggregate bonded transaction
      SignedTransaction signedTransaction = cosig1.sign(aggregateTransaction);
      // lock 10 of XPX (required to prevent spamming)
      LockFundsTransaction lockFundsTransaction = LockFundsTransaction.create(Deadline.create(2, HOURS),
            XPX.createRelative(BigInteger.valueOf(10)),
            BigInteger.valueOf(480),
            signedTransaction,
            NETWORK_TYPE);
      // sign the fund lock
      SignedTransaction lockFundsTransactionSigned = cosig1.sign(lockFundsTransaction);
      // announce the transaction
      logger.info("Sent request to lock funds: {}",
            transactionHttp.announce(lockFundsTransactionSigned).toFuture().get());
      logger.info("request confirmed: {}", listener.aggregateBondedAdded(cosig1.getAddress()).blockingFirst());
      // announce the multisig change as aggregate bounded
      logger.info("Announced the multisig change: {}",
            transactionHttp.announceAggregateBonded(signedTransaction).toFuture().get());
      logger.info("request confirmed: {}", cosig1Confirmations.blockingFirst());
   }

   @Test
   void test04CosignNewCosignatory() throws InterruptedException, ExecutionException {
      logger.info("Going to cosign the addition of cosignatory");
      accountHttp.aggregateBondedTransactions(multisigAccount.getPublicAccount()).flatMapIterable(tx -> tx)
            // .filter(tx -> !tx.signedByAccount(cosig2.getPublicAccount()))
            .map(tx -> {
               final CosignatureTransaction cosignatureTransaction = CosignatureTransaction.create(tx);
               final CosignatureSignedTransaction cosignatureSignedTransaction = cosig2
                     .signCosignatureTransaction(cosignatureTransaction);
               return transactionHttp.announceAggregateBondedCosignature(cosignatureSignedTransaction).toFuture().get();
            }).toFuture().get();
      logger.info("request confirmed: {}", cosig2Confirmations.blockingFirst());
      testMultisigAccount(true, 2, 1, 3);
   }

   private void testMultisigAccount(boolean isMultisig, int minApprovals, int minRemoval, int cosignatories)
         throws InterruptedException, ExecutionException {
      MultisigAccountInfo mai = accountHttp.getMultisigAccountInfo(multisigAccount.getAddress()).toFuture().get();
      assertEquals(isMultisig, mai.isMultisig());
      if (isMultisig) {
         assertEquals(minApprovals, mai.getMinApproval());
         assertEquals(minRemoval, mai.getMinRemoval());
         assertEquals(cosignatories, mai.getCosignatories().size());
      }
   }
}
