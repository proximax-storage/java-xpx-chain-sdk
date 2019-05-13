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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LockFileTest {

    private static final String WORKING_DIRECTORY = System.getProperty("user.dir");
    private static final File TEST_FILE_DIRECTORY = new File(WORKING_DIRECTORY, "test_files");
    private static final File TEST_EXISTING_FILE = new File(TEST_FILE_DIRECTORY, "test.lock");

    //region BeforeClass / AfterClass

    @BeforeAll
    public static void createTestFiles() throws IOException {
        final boolean result = TEST_FILE_DIRECTORY.mkdir() && TEST_EXISTING_FILE.createNewFile();

        if (!result) {
            throw new RuntimeException("unable to initialize test suite");
        }
    }

    @AfterAll
    public static void removeTestFiles() throws IOException {
        FileUtils.deleteDirectory(TEST_FILE_DIRECTORY);
    }

    //endregion

    //region tryAcquireLock

    @Test
    public void tryAcquireLockReturnsNullWhenLockFileIsInvalid() throws IOException {
        // Act:
        try (final Closeable lock = LockFile.tryAcquireLock(new File("foo\u0000.lock"))) {
            // Assert:
            MatcherAssert.assertThat(lock, IsNull.nullValue());
        }
    }

    @Test
    public void tryAcquireLockReturnsLockWhenExistingFileIsNotLocked() throws IOException {
        // Act:
        try (final Closeable lock = LockFile.tryAcquireLock(TEST_EXISTING_FILE)) {
            // Assert:
            MatcherAssert.assertThat(lock, IsNull.notNullValue());
        }
    }

    @Test
    public void tryAcquireLockReturnsLockWhenNewFileIsNotLocked() throws IOException {
        // Act:
        try (final Closeable lock = LockFile.tryAcquireLock(new File(TEST_FILE_DIRECTORY, "tryAcquireLock_new.lock"))) {
            // Assert:
            MatcherAssert.assertThat(lock, IsNull.notNullValue());
        }
    }

    @Test
    public void tryAcquireLockReturnsNullWhenExistingFileIsLocked() throws IOException {
        // Act:
        try (final Closeable lock1 = LockFile.tryAcquireLock(TEST_EXISTING_FILE)) {
            try (final Closeable lock2 = LockFile.tryAcquireLock(TEST_EXISTING_FILE)) {
                // Assert:
                MatcherAssert.assertThat(lock1, IsNull.notNullValue());
                MatcherAssert.assertThat(lock2, IsNull.nullValue());
            }
        }
    }

    //endregion

    //region isLocked

    @Test
    public void isLockedReturnsFalseWhenLockFileIsInvalid() throws IOException {
        // Act:
        final boolean isLocked = LockFile.isLocked(new File("foo\u0000.lock"));

        // Assert:
        MatcherAssert.assertThat(isLocked, IsEqual.equalTo(false));
    }

    @Test
    public void isLockedReturnsFalseWhenLockFileIsNotLocked() throws IOException {
        // Act:
        final boolean isLocked = LockFile.isLocked(TEST_EXISTING_FILE);

        // Assert:
        MatcherAssert.assertThat(isLocked, IsEqual.equalTo(false));
    }

    @Test
    public void isLockedReturnsTrueWhenLockFileIsNotLocked() throws IOException {
        // Arrange:
        try (final Closeable ignored = LockFile.tryAcquireLock(TEST_EXISTING_FILE)) {
            // Act:
            final boolean isLocked = LockFile.isLocked(TEST_EXISTING_FILE);

            // Assert:
            MatcherAssert.assertThat(isLocked, IsEqual.equalTo(true));
        }
    }

    @Test
    public void isLockedReleasesLockBeforeReturn() throws IOException {
        // Act:
        final boolean isLocked = LockFile.isLocked(TEST_EXISTING_FILE);

        try (final Closeable lock = LockFile.tryAcquireLock(TEST_EXISTING_FILE)) {
            // Assert:
            MatcherAssert.assertThat(isLocked, IsEqual.equalTo(false));
            MatcherAssert.assertThat(lock, IsNull.notNullValue());
        }
    }

    //endregion
}