package shu.nsd.service;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import shu.nsd.models.Post;

public class RequestHandler {

    private final Socket client;
    private final Consumer<Post> messageHandler;

    public RequestHandler(Socket client, Consumer <Post> messageHandler) {
        this.client = client;
        this.messageHandler = messageHandler;
        inputStream();
    }
    public void inputStream() {
        new Thread(() -> {
            try{
                BufferedInputStream inputStream = new BufferedInputStream(client.getInputStream());
                byte[] buffer = new byte[99999];
                int bytesRead;
                StringBuffer data = new StringBuffer();
                while ((bytesRead = inputStream.read(buffer)) > 0){
                    String m = new String(buffer, 0 , bytesRead, StandardCharsets.UTF_8);
                    data.append(m);
                    if(bytesRead < 99999){
                        JSONParser parser = new JSONParser();
                        try{
                            JSONObject json = (JSONObject) parser.parse(data.toString());
                            data.delete(0, data.length());
                            System.out.println(json);
                            String response = (String) json.get("response");

                            if(response.equals("posted")) parsing(json);

                            if(response.equals("loaded")){
                                JSONArray array = (JSONArray) json.get("posts");
                                if(array != null) {
                                    Iterator<JSONObject> iterator = array.iterator();
                                    while (iterator.hasNext()) {
                                        parsing(iterator.next());
                                    }
                                }
                            }

                            if(response.equals("error")){
                                System.out.println("Unknown Error");
                            }

                        }catch (Exception e){
                            System.out.println("Json parser error");
                        }
                    }
                }
                closeSocket();
            }catch (Exception e){
                e.printStackTrace();
                closeSocket();
            }
        }).start();
    }
    private void parsing(JSONObject json){
        Post post = new Post();
        post.setUsername((String) json.get("username"));
        post.setPostedTitle((String) json.get("postedTitle"));
        post.setPostedMessage((String) json.get("postedMessage"));
        post.setProfileImage((String) json.get("profileImage"));
        post.setPostedImage((String) json.get("postedImage"));
        post.setPostedDate((String) json.get("postedDate"));
        post.setChannelPosted((String) json.get("channelPosted"));
        messageHandler.accept(post);
    }
    public void request(Post post, String requestType){
        new Thread(() -> {
            try{
                BufferedOutputStream outputStream = new BufferedOutputStream(client.getOutputStream());
                JSONObject json = new JSONObject();
                json.put("request",requestType);
                if(post != null){
                    json.put("username", post.getUsername());
                    json.put("postedTitle", post.getPostedTitle());
                    json.put("postedMessage", post.getPostedMessage());
                    json.put("profileImage", post.getProfileImage());
                    json.put("postedImage", post.getPostedImage());
                    json.put("postedDate", post.getPostedDate());
                    json.put("channelPosted", post.getChannelPosted());
                }
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
