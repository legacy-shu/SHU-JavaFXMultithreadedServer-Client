package shu.nsd.models;

import java.io.Serializable;

public class Post implements Serializable {
    private String username;
    private String postedTitle;
    private String postedMessage;
    private String profileImage;
    private String postedImage;
    private String postedDate;
    private String channelPosted;
    public Post(){};

    public void setChannelPosted(String channelPosted) {
        this.channelPosted = channelPosted;
    }

    public String getChannelPosted() {
        return channelPosted;
    }

    public String getUsername() {
        return username;
    }

    public String getPostedTitle() {
        return postedTitle;
    }

    public String getPostedMessage() {
        return postedMessage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getPostedImage() {
        return postedImage;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPostedTitle(String postedTitle) {
        this.postedTitle = postedTitle;
    }

    public void setPostedMessage(String postedMessage) {
        this.postedMessage = postedMessage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setPostedImage(String postedImage) {
        this.postedImage = postedImage;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    @Override
    public String toString() {
        return "Post{" +
                "username='" + username + '\'' +
                ", postedTitle='" + postedTitle + '\'' +
                ", postedMessage='" + postedMessage + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", postedImage='" + postedImage + '\'' +
                ", postedDate='" + postedDate + '\'' +
                ", channelPosted=" + channelPosted +
                '}';
    }
}
