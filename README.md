# README

## Demo Video Link
- [Watch Demo Video](https://youtu.be/cJp7hnbg8tw)

## GitHub Repository Link
- [GitHub Repository](https://github.com/legacy-shu/SHU-JavaFXMultithreadedServer-Client.git)

## Files

### Directory Structure

- **SHUNoticeBoardClient (Folder)**
  - **out (Folder):** This folder contains all files required to execute the client program.
  - **src (Folder):** This folder includes all source files for the client.

- **SHUNoticeBoardServer (Folder)**
  - **out (Folder):** This folder includes all files required to execute the server program.
  - **src (Folder):** This folder includes all source files for the server.

- **Lib**
  - **JavaFX SDK Folder:** You will need this folder when building, compiling, or running the project.
  - **Json Simple 1.1.1 Jar File:** You will need this jar file when building, compiling, or running the project.

## Client

- **Main.java:** This class extends JavaFX's Controller class and serves as the main controller for controlling the data model and view.
  
- **Post.java:** This is a model class for mapping JSON data.
  
- **RequestHandler.java:** This class serves as a socket network manager and handles sending and receiving messages.
  
- **Util.java:** Provides functions for loading, resizing, and converting image files to base64 strings, among other things.
  
- **PostView.java:** This class displays a post and is a subclass of JavaFX's ListCell class, acting as a custom cell.
  
- **Launcher:** Another starting point class.

## Server

- **Main.java:** This is the starting class with the main method.
  
- **Responder.java:** This class is a socket network manager and handles input and output streams.
  
- **Server.java:** Creates a socket and manages clients.

## Extension

- This application implements persistence by reading and writing a JSON file in JSON Array format.

## Running the Server

- Open a command line.

- Navigate to the directory "SHUNoticeBoardServer/out" and ensure that the artifacts and production folders are present.

- Start the server: `java -jar artifacts/SHUNoticeBoardServer_jar/SHUNoticeBoardServer.jar`

## Running the Client

- Open a command line.

- Navigate to the directory "SHUNoticeBoardClient/out" and ensure that the artifacts and production folders are present.

- Start the client: `java --module-path "../../Lib/javafx-sdk-17.0.1/lib" --add-modules javafx.controls -jar artifacts/SHUNoticeBoardClient_jar/SHUNoticeBoardClient.jar`

## Build and Compile with IDE

- Open IntelliJ.

- To open the Client program, select the "SHUNoticeBoardClient" folder. To open the Server program, select the "SHUNoticeBoardServer" folder.

- Navigate to **File > Project Structure**.

- Add JavaFX SDK and the Simple JSON module from the Lib folder.

This information is also available in the [GitHub README](https://github.com/w-ryan-jung/SHU-JavaFXMultithreadedServer-Client/blob/main/README.md) file.
