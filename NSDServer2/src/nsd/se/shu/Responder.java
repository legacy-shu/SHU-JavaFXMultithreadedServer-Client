package nsd.se.shu;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Responder {

    private final Socket socket;

    public Responder(Socket socket){
        this.socket = socket;
        receive();
    }
    private void receive() {
        new Thread(() -> {
            try{
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                //InputStream inputStream = socket.getInputStream();
                byte[] buffer = new byte[99999];
                int length;
                while ((length = inputStream.read(buffer)) != -1){
                    String message = new String(buffer, 0 , length, StandardCharsets.UTF_8);
                    JSONParser parser = new JSONParser();
                    System.out.println(message);
                    JSONObject json = (JSONObject) parser.parse(message);
                    for(Responder client: Server.clients){
                        client.send(json);
                    }
                }
                closeSocket();
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
    private void send(JSONObject json) {
        new Thread(() -> {
            try{
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                //OutputStream outputStream = socket.getOutputStream();
                byte[] buffer = json.toString().getBytes(StandardCharsets.UTF_8);
                outputStream.write(buffer);
                outputStream.flush();
            }catch (Exception e){
                e.printStackTrace();
                closeSocket();
            }
        }).start();
    }
    public void closeSocket() {
        try {
            if(socket != null && !socket.isClosed()){
                socket.close();
                Server.clients.remove(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
