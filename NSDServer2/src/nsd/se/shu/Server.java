package nsd.se.shu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private final ServerSocket serverSocket;
    public static ArrayList<Responder> clients;

    public Server(ServerSocket serverSocket){
        clients = new ArrayList<>();
        this.serverSocket = serverSocket;
    }

    public void listen() {
        while (true){
            try {
                Socket socket = serverSocket.accept();
                clients.add(new Responder(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
