# Universal Storage Java API
## File System provider

Universal storage provides you an interface for storing files according to your needs.  With this Universal Storage Java API, you will be able to develop programs in Java and use an interface for storing your files within a file system storage.

<hr>

**This documentation has the following content:**

1. [Maven project](maven-project)
2. [Test API](#test-api)
3. [Settings](#settings)
  3.1. [Installation](#installation)
4. [How to use](#how-to-use)

# Maven project
This API follows the Maven structure to ease its installation within your project.

# Test API
If you want to test the API, follow these steps:

1. Create a folder and copy its the absolute path.  This folder will be your storage root target.
2. Create a folder and copy its the absolute path.  This folder will be your tmp folder.
3. Open with a text editor the settings.json located on test/resources/settings.json
```json
{
   "provider": "file.system",
   "root": "/home/user/storage",
   "tmp": "/home/user/tmp"
}
```
4. Paste the absolute paths, the root's path and the tmp's path.
5. Save the settings.json file.

**Now execute the following command:**

`mvn clean test`

# Settings
**These are the steps for setting up Universal Storage in your project:**
1. You must create a file called settings.json (can be any name) and paste the following. 
```json
{
   "provider": "file.system",
   "root": "/home/user/storage",
   "tmp": "/home/user/tmp"
}
```
2. The root and tmp keys are the main data to be filled, create two folders representing each one root and tmp.
The root folder is the storage where the files will be stored.
The tmp folder is where temporary files will be stored.
3. Save the file settings.json

# How to use
