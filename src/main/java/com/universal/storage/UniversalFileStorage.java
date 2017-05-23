package com.universal.storage;

import com.universal.util.PathValidator;
import com.universal.error.UniversalIOException;
import com.universal.storage.settings.UniversalSettings;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
public class UniversalFileStorage extends UniversalStorage {
    /**
     * This constructor receives the settings for this new FileStorage instance.
     * 
     * @param settings for this new FileStorage instance.
     */
    public UniversalFileStorage(UniversalSettings settings) {
        super(settings);
    }

    /**
     * This method stores a file within the storage provider according to the current settings.
     * The method will replace the file if already exists within the root.
     * 
     * For exemple:
     * 
     * path == null
     * File = /var/wwww/html/index.html
     * Root = /storage/
     * Copied File = /storage/index.html
     * 
     * path == "myfolder"
     * File = /var/wwww/html/index.html
     * Root = /storage/
     * Copied File = /storage/myfolder/index.html
     * 
     * If this file is a folder, a error will be thrown informing that should call the createFolder method.
     * 
     * Validations:
     * Validates if root is a folder.
     * 
     * @param file to be stored within the storage.
     * @param path is the path for this new file within the root.
     * @throws UniversalIOException when a specific IO error occurs.
     */
    void storeFile(File file, String path) throws UniversalIOException {
        validateRoot(this.settings);

        if (file.isDirectory()) {
            throw new UniversalIOException(file.getName() + " is a folder.  You should call the createFolder method.");
        }

        try {
            FileUtils.copyFileToDirectory(file, new File(this.settings.getRoot() + (path == null ? "" : path)));
        } catch(Exception e) {
            throw new UniversalIOException(e.getMessage());
        }
    }

    /**
     * This method stores a file according to the provided path within the storage provider according to the current settings.
     * 
     * @param path pointing to the file which will be stored within the storage.
     * @throws UniversalIOException when a specific IO error occurs.
     */
    void storeFile(String path) throws UniversalIOException {
        this.storeFile(new File(path), null);
    }

    /**
     * This method stores a file according to the provided path within the storage provider according to the current settings.
     * 
     * @param path pointing to the file which will be stored within the storage.
     * @param targetPath is the path within the storage.
     * 
     * @throws UniversalIOException when a specific IO error occurs.
     */
    void storeFile(String path, String targetPath) throws UniversalIOException {
        PathValidator.validatePath(path);
        PathValidator.validatePath(targetPath);

        this.storeFile(new File(path), targetPath);
    }

    /**
     * This method removes a file from the storage.  This method will use the path parameter 
     * to localte the file and remove it from the storage.
     * 
     * Root = /storage/
     * path = myfile.txt
     * Target = /storage/myfile.txt
     * 
     * Root = /storage/
     * path = myfolder/myfile.txt
     * Target = /storage/myfolder/myfile.txt 
     * 
     * @param path is the file's path within the storage.  
     * @throws UniversalIOException when a specific IO error occurs.
     */
    void removeFile(String path) throws UniversalIOException {
        PathValidator.validatePath(path);

        validateRoot(this.settings);

        File file = new File(this.settings.getRoot() + path);

        if (file.isDirectory()) {
            throw new UniversalIOException(file.getName() + " is a folder.  You should call the removeFolder method.");
        }

        try {
            FileUtils.forceDelete(file);
        } catch(Exception e) {
            throw new UniversalIOException(e.getMessage());
        }
    }

    /**
     * This method creates a new folder within the storage using the passed path. If the new folder name already
     * exists within the storage, this  process will skip the creation step.
     * 
     * Root = /storage/
     * path = /myFolder
     * Target = /storage/myFolder
     * 
     * Root = /storage/
     * path = /folders/myFolder
     * Target = /storage/folders/myFolder
     * 
     * @param path is the folder's path.
     * @param storeFiles is a flag to store the files after folder creation.
     * 
     * @throws UniversalIOException when a specific IO error occurs.
     * @throws IllegalArgumentException is path has an invalid value.
     */
    void createFolder(String path) throws UniversalIOException {
        PathValidator.validatePath(path);

        validateRoot(this.settings);
        
        try {
            File newFolder = new File(this.settings.getRoot() + path);

            if (!"".equals(path.trim()) && !newFolder.exists()) {
                newFolder.mkdir();
            }
        } catch(Exception e) {
            throw new UniversalIOException(e.getMessage());
        }
    }

    /**
     * This method removes the folder located on that path.
     * 
     * Root = /storage/
     * path = myFolder
     * Target = /storage/myFolder
     * 
     * Root = /storage/
     * path = folders/myFolder
     * Target = /storage/folders/myFolder
     * 
     * @param path of the folder.
     */
    void removeFolder(String path) throws UniversalIOException {
        PathValidator.validatePath(path);

        validateRoot(this.settings);

        if ("".equals(path.trim())) {
            return;
        }

        File file = new File(this.settings.getRoot() + path);

        if (!file.isDirectory()) {
            throw new UniversalIOException(file.getName() + " is a file.  You should call the removeFile method.");
        }

        try {
            FileUtils.forceDelete(file);
        } catch(Exception e) {
            throw new UniversalIOException(e.getMessage());
        }
    }

    /**
     * This method retrieves a file from the storage.
     * The method will retrieve the file according to the passed path.  
     * A file will be stored within the settings' tmp folder.
     * 
     * @param path in context.
     * @returns a file pointing to the retrieved file.
     */
    public File retrieveFile(String path) throws UniversalIOException {
        PathValidator.validatePath(path);

        validateRoot(this.settings);

        if ("".equals(path.trim())) {
            return null;
        }

        File file = new File(this.settings.getRoot() + path);
System.out.println("################# " + new File(this.settings.getRoot() + path).getAbsolutePath());
        if (!file.exists()) {
            throw new UniversalIOException(file.getName() + " doesn't exist.");
        }

        if (file.isDirectory()) {
            throw new UniversalIOException(file.getName() + " is a folder.");
        }

        File tmp = new File(this.settings.getTmp());
        try {
            FileUtils.copyFileToDirectory(file, tmp);
        } catch(Exception e) {
            throw new UniversalIOException(e.getMessage());
        }

        return new UniversalFile(this.settings.getTmp() + file.getName());
    }

    /**
     * This method retrieves a file from the storage as InputStream.
     * The method will retrieve the file according to the passed path.  
     * A file will be stored within the settings' tmp folder.
     * 
     * @param path in context.
     * @returns an InputStream pointing to the retrieved file.
     */
    public InputStream retrieveFileAsStream(String path) throws UniversalIOException {
        try {
            return new FileInputStream(retrieveFile(path));
        } catch (FileNotFoundException e) {
            throw new UniversalIOException(e.getMessage());
        }
    }

    /**
     * This method cleans the context of this storage.  This method doesn't remove any file from the storage.
     * The method will clean the tmp folder to release disk usage.
     */
    public void clean() throws UniversalIOException  {
        try {
           // FileUtils.cleanDirectory(new File(this.settings.getTmp()));
        } catch (Exception e) {
            throw new UniversalIOException(e.getMessage());
        }
    }
}