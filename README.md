# Universal Storage Java API
## File System provider

Universal storage provides you an interface for storing files according to your needs.  With this Universal Storage Java API, you will be able to develop programs in Java and use an interface for storing your files within a file system storage.

<hr>

**This documentation has the following content:**

1. [Test API](#test-api)
2. [Settings](#settings)
  1. [Installation](#installation)
3. [How to use](#how-to-use)

# Test API
If you want to test the API, follow these steps:

1. Create a folder and copy its the absolute path.  This folder will be your storage root target.
2. Create a folder and copy its the absolute path.  This folder will be your tmp folder.
2. Open the settings.json on test/resources/settings
`json
{
	"provider": "file.system",
	"root": "C:/storage",
	"tmp": "C:/tmp"
}
`

# Settings

# How to use
