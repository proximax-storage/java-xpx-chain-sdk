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

package io.proximax.core.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;

public class ExceptionUtilsTest {

   // region propagate

   @Test
   public void propagateReturnsResultOnSuccess() {
      // Act:
      final int result = ExceptionUtils.propagate(() -> 7);

      // Assert:
      MatcherAssert.assertThat(result, IsEqual.equalTo(7));
   }

   @Test
   public void propagateAllowsRuntimeExceptionsToPropagate() {
      // Act:
      assertThrows(IllegalArgumentException.class, () -> ExceptionUtils.propagate(() -> {
         throw new IllegalArgumentException();
      }));
   }

   @Test
   public void propagateWrapsCheckedExceptionsInRuntimeExceptionByDefault() {
      // Act:
      assertThrows(RuntimeException.class, () -> ExceptionUtils.propagate(() -> {
         throw new IOException();
      }));
   }

   @Test
   public void propagateCanWrapCheckedExceptionsInCustomRuntimeException() {
      // Act:
      assertThrows(IllegalArgumentException.class, () -> ExceptionUtils.propagate(() -> {
         throw new IOException();
      }, IllegalArgumentException::new));
   }

   @Test
   public void propagateUnwrapsUncheckedExecutionExceptions() {
      // Act:
      assertThrows(IllegalArgumentException.class, () -> ExceptionUtils.propagate(() -> {
         throw new MockExecutionException(new IllegalArgumentException());
      }));
   }

   @Test
   public void propagateWrapsCheckedExecutionExceptionsInRuntimeExceptionByDefault() {
      // Act:
      assertThrows(RuntimeException.class, () -> ExceptionUtils.propagate(() -> {
         throw new MockExecutionException(new IOException());
      }));
   }

   @Test
   public void propagateCanWrapCheckedExecutionExceptionsInCustomRuntimeException() {
      // Act:
      assertThrows(IllegalArgumentException.class, () -> ExceptionUtils.propagate(() -> {
         throw new MockExecutionException(new IOException());
      }, IllegalArgumentException::new));
   }

   @Test
   public void propagateMapsInterruptedExceptionToIllegalStateException() throws InterruptedException {
      // Arrange:
      final InterruptedExceptionTestRunner runner = new InterruptedExceptionTestRunner(
            () -> ExceptionUtils.propagate(() -> {
               Thread.sleep(1000);
               return null;
            }));

      // Act:
      runner.run();

      // Assert:
      MatcherAssert.assertThat(runner.getUnhandledException(), IsNull.notNullValue());
      MatcherAssert.assertThat(runner.getUnhandledException(), IsInstanceOf.instanceOf(IllegalStateException.class));
   }

   @Test
   public void propagateSetsThreadInterruptFlagWhenMappingInterruptedException() throws InterruptedException {
      // Arrange:
      final InterruptedExceptionTestRunner runner = new InterruptedExceptionTestRunner(
            () -> ExceptionUtils.propagate(() -> {
               Thread.sleep(1000);
               return null;
            }));

      // Act:
      runner.run();

      // Assert:
      MatcherAssert.assertThat(runner.isInterruptedPreRun(), IsEqual.equalTo(false));
      MatcherAssert.assertThat(runner.isInterruptedPostRun(), IsEqual.equalTo(true));
   }

   // endregion

   // region propagateVoid

   @Test
   public void propagateVoidDoesNotThrowExceptionOnSuccess() {
      // Act:
      ExceptionUtils.propagateVoid(() -> {
      });

      // Assert: (no exception)
   }

   @Test
   public void propagateVoidAllowsRuntimeExceptionsToPropagate() {
      // Act:
      assertThrows(IllegalArgumentException.class, () -> ExceptionUtils.propagateVoid(() -> {
         throw new IllegalArgumentException();
      }));
   }

   @Test
   public void propagateVoidWrapsCheckedExceptionsInRuntimeExceptionByDefault() {
      // Act:
      assertThrows(RuntimeException.class, () -> ExceptionUtils.propagateVoid(() -> {
         throw new IOException();
      }));
   }

   @Test
   public void propagateVoidCanWrapCheckedExceptionsInCustomRuntimeException() {
      // Act:
      assertThrows(IllegalArgumentException.class, () -> ExceptionUtils.propagateVoid(() -> {
         throw new IOException();
      }, IllegalArgumentException::new));
   }

   @Test
   public void propagateVoidUnwrapsUncheckedExecutionExceptions() {
      // Act:
      assertThrows(IllegalArgumentException.class, () -> ExceptionUtils.propagateVoid(() -> {
         throw new MockExecutionException(new IllegalArgumentException());
      }));
   }

   @Test
   public void propagateVoidWrapsCheckedExecutionExceptionsInRuntimeExceptionByDefault() {
      // Act:
      assertThrows(RuntimeException.class, () -> ExceptionUtils.propagateVoid(() -> {
         throw new MockExecutionException(new IOException());
      }));
   }

   @Test
   public void propagateVoidCanWrapCheckedExecutionExceptionsInCustomRuntimeException() {
      // Act:
      assertThrows(IllegalArgumentException.class, () -> ExceptionUtils.propagateVoid(() -> {
         throw new MockExecutionException(new IOException());
      }, IllegalArgumentException::new));
   }

   @Test
   public void propagateVoidMapsInterruptedExceptionToIllegalStateException() throws InterruptedException {
      // Arrange:
      final InterruptedExceptionTestRunner runner = new InterruptedExceptionTestRunner(() -> {
         ExceptionUtils.propagateVoid(() -> Thread.sleep(1000));
         return null;
      });

      // Act:
      runner.run();

      // Assert:
      MatcherAssert.assertThat(runner.getUnhandledException(), IsNull.notNullValue());
      MatcherAssert.assertThat(runner.getUnhandledException(), IsInstanceOf.instanceOf(IllegalStateException.class));
   }

   @Test
   public void propagateVoidSetsThreadInterruptFlagWhenMappingInterruptedException() throws InterruptedException {
      // Arrange:
      final InterruptedExceptionTestRunner runner = new InterruptedExceptionTestRunner(() -> {
         ExceptionUtils.propagateVoid(() -> Thread.sleep(1000));
         return null;
      });

      // Act:
      runner.run();

      // Assert:
      MatcherAssert.assertThat(runner.isInterruptedPreRun(), IsEqual.equalTo(false));
      MatcherAssert.assertThat(runner.isInterruptedPostRun(), IsEqual.equalTo(true));
   }

   // endregion

   private class InterruptedExceptionTestRunner {

      private final Thread blockingThread;

      private boolean isInterruptedPreRun;
      private boolean isInterruptedPostRun;
      private Throwable unhandledException;

      public InterruptedExceptionTestRunner(final Supplier<Void> supplier) {
         this.blockingThread = new Thread(() -> {
            try {
               supplier.get();
            } finally {
               this.isInterruptedPostRun = Thread.currentThread().isInterrupted();
            }
         });

         this.blockingThread.setUncaughtExceptionHandler((t, e) -> this.unhandledException = e);
      }

      public boolean isInterruptedPreRun() {
         return this.isInterruptedPreRun;
      }

      public boolean isInterruptedPostRun() {
         return this.isInterruptedPostRun;
      }

      public Throwable getUnhandledException() {
         return this.unhandledException;
      }

      public void run() throws InterruptedException {
         this.isInterruptedPreRun = blockingThread.isInterrupted();
         this.blockingThread.start();
         Thread.sleep(100);

         this.blockingThread.interrupt();
         this.blockingThread.join();
      }
   }

   private class MockExecutionException extends ExecutionException {

      public MockExecutionException(final Throwable cause) {
         super(cause);
      }
   }
}
