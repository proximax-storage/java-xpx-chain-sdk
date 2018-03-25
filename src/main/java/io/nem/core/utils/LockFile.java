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

package io.nem.core.utils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

/**
 * Static class that exposes functions for interacting with lock files.
 */
public class LockFile {

    /**
     * Tries to acquire a file lock for the specified file.
     *
     * @param lockFile The lock file.
     * @return A handle to the file lock if acquired, or null otherwise.
     */
    public static Closeable tryAcquireLock(final File lockFile) {
        FileLockHandle handle = null;
        try {
            handle = new FileLockHandle(lockFile);

            // try to acquire the lock 5 times
            for (int i = 0; i < 5; ++i) {
                if (handle.tryLock()) {
                    return handle;
                }

                ExceptionUtils.propagateVoid(() -> Thread.sleep(10));
            }

            return null;
        } catch (final IOException | OverlappingFileLockException e) {
            return null;
        } finally {
            if (null != handle && null == handle.lock) {
                try {
                    handle.close();
                } catch (final IOException ignored) {
                }
            }
        }
    }

    /**
     * Determines whether or not the specified file is locked.
     *
     * @param lockFile The lock file.
     * @return true if the file is locked, false otherwise.
     */
    public static boolean isLocked(final File lockFile) {
        try (final FileLockHandle handle = new FileLockHandle(lockFile)) {
            return !handle.tryLock();
        } catch (final OverlappingFileLockException e) {
            return true;
        } catch (final IOException e) {
            return false;
        }
    }

    private static class FileLockHandle implements Closeable {
        private final RandomAccessFile file;
        private FileLock lock;

        public FileLockHandle(final File lockFile) throws IOException {
            this.file = new RandomAccessFile(lockFile, "rw");
        }

        private boolean tryLock() throws IOException {
            this.lock = this.file.getChannel().tryLock();
            return null != this.lock;
        }

        @Override
        public void close() throws IOException {
            if (null != this.lock) {
                this.lock.close();
            }

            this.file.close();
        }
    }
}
