package com.universal.storage;

import junit.framework.TestCase;
import java.io.File;

import com.universal.error.UniversalIOException;
import com.universal.error.UniversalStorageException;
import org.apache.commons.io.FileUtils;
import com.universal.util.FileUtil;
import com.universal.storage.settings.UniversalSettings;

/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Dynamicloud
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * This class is the implementation of a storage that will manage files as a default file system.
 * This implementation will manage file using a setting to store files like so.
 */
public class TestUniversalFileStorage extends TestCase {
    private static UniversalStorage us = null;
    protected void setUp() {
        try {
            us = UniversalStorage.Impl.
                    getInstance(new UniversalSettings(new File("src/test/resources/settings.json")));

            us.registerListener(new UniversalStorageListenerAdapter() {
                public void onFolderCreated(UniversalStorageData data) {
                    System.out.println(data.toString());
                }

                public void onFileStored(UniversalStorageData data) {
                    System.out.println(data.toString());
                }

                public void onError(UniversalIOException error) {
                    System.out.println("#### - " + error.getMessage());
                }
            });
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void setUpTest(String fileName, String folderName) {
        try {
            File newFile = new File(System.getProperty("user.home"), fileName);
            if (!newFile.exists()) {
                try {
                    newFile.createNewFile();
                } catch (Exception e) {
                    fail(e.getMessage());
                }
            }

            us.storeFile(new File(System.getProperty("user.home"), fileName), folderName);
            us.storeFile(new File(System.getProperty("user.home"), fileName));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testRetrieveFileAsFileSystemProvider() {
        String fileName = System.nanoTime() + ".txt";
        try {
            setUpTest(fileName, "retrieve/innerfolder");
            us.retrieveFile("retrieve/innerfolder/" + fileName);
        } catch (UniversalStorageException e) {
            fail(e.getMessage());
        }

        try {
            us.retrieveFile("retrieve/innerfolder/settings.jsontxt");
            fail("This method should throw an error.");
        } catch (UniversalStorageException ignore) {
            
        }

        try {
            FileUtils.copyInputStreamToFile(us.retrieveFileAsStream("retrieve/innerfolder/" + fileName), 
                new File(FileUtil.completeFileSeparator(System.getProperty("user.home")) + fileName));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * This test will execute the remove file process using a FILE_SYSTEM provider.
     */
    public void testRemoveFileAsFileSystemProvider() {
        try {
            String fileName = System.nanoTime() + ".txt";
            setUpTest(fileName, "remove/innerfolder");

            us.removeFile(fileName);
            us.removeFile("remove/innerfolder/" + fileName);
        } catch (UniversalStorageException e) {
            fail(e.getMessage());
        }
    }

    /**
     * This test will execute the create folder process using a FILE_SYSTEM provider.
     */
    public void testCreateFolderRemoveAsFileSystemProvider() {
        try {
            us.createFolder("myNewFolder");
            us.removeFolder("myNewFolder");
        } catch (UniversalStorageException e) {
            fail(e.getMessage());
        }
    }

    /**
     * This test will clean the storage's context.
     */
    public void testCleanStorageAsFileSystemProvider() {
        try {
            us.clean();
        } catch (UniversalStorageException e) {
            fail(e.getMessage());
        }
    }

    /**
     * This test will wipe the storage's context.
     */
    public void testWipeStorageAsFileSystemProvider() {
        try {
            us.wipe();
        } catch (UniversalStorageException e) {
            fail(e.getMessage());
        }
    }
}