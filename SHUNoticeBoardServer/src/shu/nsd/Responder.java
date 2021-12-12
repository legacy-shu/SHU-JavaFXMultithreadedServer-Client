package shu.nsd;

import org.json.simple.JSONArray;
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
                BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
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
                            String request = (String) json.get("request");

                            if(request.equals("post")){
                                synchronized (Server.savedposts){
                                    Server.savedposts.add(response(json));
                                    Server.save();
                                }
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("response", "posted");
                                for(Responder client: Server.clients){
                                    client.send(response(jsonObject,json));
                                }
                            }

                            if(request.equals("load")){
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("response", "loaded");
                                for(Responder client: Server.clients){
                                    synchronized (Server.savedposts){
                                        client.send(response(jsonObject,Server.savedposts));
                                    }
                                }
                            }

                        }catch (Exception e){
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("response", "error");
                            for(Responder client: Server.clients){
                                client.send(jsonObject);
                            }
                        }
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
                BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
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
    private JSONObject response(JSONObject j1, JSONArray array){
        j1.put("posts", array);
        return j1;
    }
    private JSONObject response(JSONObject j1, JSONObject j2){
        j1.put("username", j2.get("username"));
        j1.put("postedTitle", j2.get("postedTitle"));
        j1.put("postedMessage", j2.get("postedMessage"));
        j1.put("profileImage", j2.get("profileImage"));
        j1.put("postedImage", j2.get("postedImage"));
        j1.put("postedDate", j2.get("postedDate"));
        j1.put("channelPosted", j2.get("channelPosted"));
        return j1;
    }
    private JSONObject response(JSONObject json){
        return response(new JSONObject(), json);
    }
}
