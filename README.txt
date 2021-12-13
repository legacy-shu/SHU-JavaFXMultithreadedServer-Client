README
========

Demo Video Link : https://youtu.be/cJp7hnbg8tw

Github Link : https://github.com/w-ryan-jung/SHU-JavaFXMultithreadedServer-Client.git

FIELS

	- Directory structure
		
		- SHUNoticeBoardClient (Folder)
			- out (Folder) : This folder includes all files for execute client program
			- src (Folder) : This folder includes all source files for client
			
		- SHUNoticeBoardServer(Folder)
			- out (Folder) : This folder includes all files for execute server program
			- src (Folder) : This folder includes all source files for server 

		- Lib
			- JavaFX sdk folder : when you build or compile or run you need this folder
			- Json simple 1.1.1 jar file : when you build or compile or run you need this folder
	
	- Client
		
		- Main.java				Extends Javafx's Controller class and it is main controller for control the data model and view
		
		- Post.java					Model class for mapping json data 
		
		- RequestHandler.java		Socket network manager class, handle with send and receive messages  
		
		- Util.java					Image file load, resize etc and convert image to base64 string and so on
		
		- PostView.java				Display a post, Subclass of Javafx's ListCell class as custom cell
		
		- Launcher				Another start point class 

	- Server
		
		- Main.java				Start class with main method
		
		- Responder.java			Socket network manager class, handle with input and output stream.
		
		- Server.java				Create a socket and manage clients


Extension

	- This application is implemented the persistence by reading and writing a json file as json format based Json Array type object.

RUNNING THE SERVER 
	
	- Open a command line.
	
	- Change to the directory "SHUNoticeBoardServer/out" and check the artifacts and production folders are resided
	
	- Starting the server : java -jar artifacts/SHUNoticeBoardServer_jar/SHUNoticeBoardServer.jar 

RUNNING THE CLIENT
	
	- Open a command line.
	
	- Change to the directory "SHUNoticeBoardClient/out" and check the artifacts and production folders are resided
	
	- Starting the client : java --module-path "../../Lib/javafx-sdk-17.0.1/lib" --add-modules javafx.controls -jar artifacts/SHUNoticeBoardClient_jar/SHUNoticeBoardClient.jar 

BUILD AND COMPILE WITH IDE
	
 	-Open Intellij
	
	-Select the folder SHUNoticeBoardClient to open Client program or SHUNoticeBoardServer to open Server program

	- File/ Project Structure 

 	- Add Javafx sdk and simple json module from Lib folder
	
	- You can build and run  