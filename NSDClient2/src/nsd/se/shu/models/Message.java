package nsd.se.shu.models;

public class Message {
    private String message;
    private String image;
    private String nickName;
    public Message(String message, String nickName, String image){
        this.message = message;
        this.image = image;
        this.nickName = nickName;
    }

    public String getMessage() {
        return message;
    }

    public String getImage() {
        return image;
    }

    public String getNickName() {
        return nickName;
    }
}
