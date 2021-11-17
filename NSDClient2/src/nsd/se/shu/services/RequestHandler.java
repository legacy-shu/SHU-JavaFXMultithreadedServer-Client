package nsd.se.shu.services;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import nsd.se.shu.models.Message;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class RequestHandler {

    private final Socket client;
    private final Consumer<Message> messageHandler;

    public RequestHandler(Socket client, Consumer <Message> messageHandler) {
        this.client = client;
        this.messageHandler = messageHandler;
        inputStream();
    }
    public void inputStream() {
        new Thread(() -> {
            try{
                DataInputStream inputStream = new DataInputStream(client.getInputStream());
                //InputStream inputStream = client.getInputStream();
                byte[] buffer = new byte[99999];
                int length;
                while ((length = inputStream.read(buffer)) != -1){
                    String m = new String(buffer, 0 , length, StandardCharsets.UTF_8);
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(m);
                    Message message = new Message((String) json.get("message"), (String) json.get("nickName") ,(String) json.get("image"));
                    messageHandler.accept(message);
                }
                closeSocket();
            }catch (Exception e){
                e.printStackTrace();
                closeSocket();
            }
        }).start();
    }
    public void outputStream(Message message){
        new Thread(() -> {
            try{
                DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
                //OutputStream outputStream = client.getOutputStream();
                JSONObject json = new JSONObject();
                json.put("message", message.getMessage());
                json.put("nickName", message.getNickName());
                json.put("image", message.getImage());
                System.out.println(json.toString());
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
            if(isConnected())
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        return client != null && !client.isClosed();
    }
}
