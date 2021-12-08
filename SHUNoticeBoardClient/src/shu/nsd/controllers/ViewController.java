package shu.nsd.controllers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import shu.nsd.models.*;
import shu.nsd.service.RequestHandler;
import shu.nsd.utils.Util;
import shu.nsd.views.PostView;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class ViewController extends Application {
    private final ObservableList<Post> posts = FXCollections.observableArrayList();
    private final ArrayList<Post> allPosts = new ArrayList<>();
    private RequestHandler socketManager;

    private String loggedChannel;
    private String userNameString;
    private String postImages64String;
    private String profileImages64String;

    private ListView<Post> listView;
    private ImageView postingImage;
    private ImageView profileImageView;

    private TextField titleField;
    private TextArea messageArea;
    private Button selectImageButton;
    private Button profileEditButton;
    private Button onButton;
    private Button postButton;

    private static CheckBox channelA;
    private static CheckBox channelB;
    private static CheckBox channelC;

    private File selectedFile;
    private FileChooser fileChooser;

    @Override
    public void start(Stage stage) {

        showLogin();

        setUI(stage);

        registerAllEvents(stage);

        setDefaultImage();

        requestConnection();

        socketManager.request(null,"load");

    }

    private void requestConnection(){
        if(onButton.getText().equals("Off")){
            socketManager.closeSocket();
            onButton.setText("On");
        }else {
            try {
                Socket socket = new Socket("127.0.0.1", 12345);
                socketManager = new RequestHandler(socket, (post) -> Platform.runLater(() -> {
                    allPosts.add(post);
                    refreshView();
                }));
                if (socketManager.isConnected()) {
                    System.out.println("Connected");
                    onButton.setText("Off");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void registerAllEvents(Stage stage){

        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);

        profileEditButton.setOnAction(actionEvent -> {
            selectedFile = fileChooser.showOpenDialog(stage);
            if(selectedFile != null){
                profileImages64String = Util.imageToBase64(selectedFile.getPath());
                profileImageView = getIamgeView(selectedFile.getPath(),90);
                makeRoundIamgeView(profileImageView);
                profileEditButton.setGraphic(profileImageView);
            }
        });

        selectImageButton.setOnAction(actionEvent -> {
            selectedFile = fileChooser.showOpenDialog(stage);
            if(selectedFile != null){
                postImages64String = Util.imageToBase64(selectedFile.getPath());
                postingImage = getIamgeView(selectedFile.getPath(),20);
            }else{
                postingImage = getIamgeView("src/resources/add.png",20);
            }
            selectImageButton.setGraphic(postingImage);
        });

        postButton.setOnAction(actionEvent -> {

            if(socketManager == null || !socketManager.isConnected()) {
                showDialog("Unconnected");
                return;
            }

            if (titleField.getText().equals("")) {
                showDialog("Title is empty");
                return;
            }
            if (messageArea.getText().equals("")) {
                showDialog("Message is empty");
                return;
            }

            Post post = new Post();
            post.setUsername(userNameString);
            post.setProfileImage(profileImages64String);
            post.setPostedImage(postImages64String);
            post.setPostedTitle(titleField.getText());
            post.setPostedMessage(messageArea.getText());
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(new Date());
            post.setPostedDate(timeStamp);
            post.setChannelPosted(loggedChannel);
            System.out.println(post);
            socketManager.request(post, "post");
        });

        onButton.setOnAction(actionEvent -> {
            requestConnection();
            if(socketManager != null && socketManager.isConnected()) showDialog("Connected");
            else showDialog("Unconnected");
        });

        channelA.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                refreshView();
            }
        });
        channelB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                refreshView();
            }
        });
        channelC.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                refreshView();
            }
        });

    }

    private void setDefaultImage(){
        profileImages64String = Util.imageToBase64("src/resources/user.png");
    }
    private void setUI(Stage stage){
        HBox layout = new HBox(setLeftView(stage), setRightView(stage));
        Scene scene = new Scene(layout, 600, 430);
        stage.setScene(scene);
        stage.setTitle(String.format("You are login channel %s (127.0.0.1:12345)",loggedChannel));
        stage.setMaxHeight(430);
        stage.setMinHeight(430);
        stage.setMaxWidth(600);
        stage.setMinWidth(600);
        stage.show();
    }
    private VBox setLeftView(Stage stage){

        Label aTtitle = new Label("Your subscribe channel list");
        aTtitle.setTextFill(Color.color(1,1,1));

        HBox channels = new HBox(channelA, channelB, channelC);
        channels.setAlignment(Pos.CENTER);
        channels.setSpacing(10);

        VBox toolBar = new VBox(aTtitle, channels);
        toolBar.setPrefHeight(40);
        toolBar.setAlignment(Pos.CENTER);
        toolBar.prefWidthProperty().bind(stage.widthProperty().multiply(0.5));
        toolBar.setBackground(new Background(new BackgroundFill(Color.DARKCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        toolBar.setSpacing(5);
        Label userNameLabel = new Label(userNameString);
        userNameLabel.setTextFill(Color.color(1,1,1));

        profileImageView = getIamgeView("src/resources/user.png",90);
        makeRoundIamgeView(profileImageView);

        profileEditButton = new Button("Edit",profileImageView);
        profileEditButton.setStyle("-fx-content-display: bottom;");

        VBox profilePane = new VBox(toolBar, profileEditButton, userNameLabel);
        profilePane.setAlignment(Pos.CENTER);
        profilePane.setSpacing(10);

        Label title = new Label("Title");
        title.setTextFill(Color.color(1,1,1));
        titleField = new TextField();
        VBox titlePane = new VBox(title, titleField);

        Label message = new Label("Message");
        message.setTextFill(Color.color(1,1,1));
        messageArea = new TextArea();
        messageArea.setPrefHeight(80);
        VBox messagePane = new VBox(message, messageArea);

        postingImage = getIamgeView("src/resources/add.png",20);
        selectImageButton = new Button("Add Image",postingImage);
        selectImageButton.setStyle("-fx-content-display: left;");

        ImageView postingImage =  getIamgeView("src/resources/post.png",20);
        postButton = new Button("Post",postingImage);
        postButton.setStyle("-fx-content-display: left;");

        ImageView onOffIamgeView = getIamgeView("src/resources/switch.png",20);
        onButton = new Button("On",onOffIamgeView);
        onButton.setStyle("-fx-content-display: left;");


        HBox bottomPane = new HBox(onButton, selectImageButton, postButton);
        bottomPane.setAlignment(Pos.BOTTOM_CENTER);
        bottomPane.setSpacing(8);

        VBox leftViewGroup = new VBox(profilePane, titlePane, messagePane, bottomPane);
        leftViewGroup.setSpacing(10);
        leftViewGroup.setPadding(new Insets(10));
        leftViewGroup.setBackground(new Background(new BackgroundFill(Color.DARKCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        leftViewGroup.prefWidthProperty().bind(stage.widthProperty().multiply(0.5));

        return leftViewGroup;
    }
    private VBox setRightView(Stage stage){

        Label toolBartitle = new Label("Hallam Posting Board");
        toolBartitle.setTextFill(Color.color(1,1,1));

        VBox toolBar = new VBox(toolBartitle);
        toolBar.setPrefHeight(40);
        toolBar.setAlignment(Pos.CENTER);
        toolBar.prefWidthProperty().bind(stage.widthProperty().multiply(0.5));
        toolBar.setBackground(new Background(new BackgroundFill(Color.DARKCYAN, CornerRadii.EMPTY, Insets.EMPTY)));

        listView = new ListView<>(posts);
        listView.setCellFactory(listView -> new PostView());
        listView.setStyle("-fx-control-inner-background: #a2c4c9;");

        return new VBox(toolBar,listView);
    }
    private void showLogin(){

        channelA = new CheckBox("Channel A");
        channelB = new CheckBox("Channel B");
        channelC = new CheckBox("Channel C");

        TextInputDialog loginDialog = new TextInputDialog("hallam");
        loginDialog.setTitle("[127.0.0.1 : 12345]");
        loginDialog.setHeaderText("Welcome Hallam Posting Board");
        loginDialog.setContentText("Please enter your name");
        loginDialog.setGraphic(new ImageView(this.getClass().getResource("/resources/logo.png").toString()));
        loginDialog.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(true);

        Label channelTitle = new Label("Please check a channel you want to login");
        ToggleGroup radioGroup = new ToggleGroup();
        RadioButton radioButton1 = new RadioButton("Channel A");
        RadioButton radioButton2 = new RadioButton("Channel B");
        RadioButton radioButton3 = new RadioButton("Channel C");
        radioButton1.setUserData("A");
        radioButton2.setUserData("B");
        radioButton3.setUserData("C");
        radioButton1.setToggleGroup(radioGroup);
        radioButton2.setToggleGroup(radioGroup);
        radioButton3.setToggleGroup(radioGroup);
        radioGroup.selectToggle(radioButton1);
        loggedChannel = (String) radioGroup.getSelectedToggle().getUserData();
        channelA.setSelected(radioButton1.isSelected());
        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                loggedChannel = (String) radioGroup.getSelectedToggle().getUserData();
                channelA.setSelected(radioButton1.isSelected());
                channelB.setSelected(radioButton2.isSelected());
                channelC.setSelected(radioButton3.isSelected());
            }
        });
        HBox radioButtons = new HBox(radioButton1,radioButton2,radioButton3);
        VBox contents = new VBox(loginDialog.getDialogPane().getContent(), channelTitle, radioButtons);
        contents.setSpacing(10);

        loginDialog.getDialogPane().setContent(contents);

        Optional<String> result = loginDialog.showAndWait();
        result.ifPresent(name -> {
            userNameString = name;
        });
    }
    private ImageView getIamgeView(String path, int size){
        ImageView imageView = new ImageView(Util.getImage(path));
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        return imageView;
    }
    private void makeRoundIamgeView(ImageView imageView){
        Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        clip.setArcWidth(90);
        clip.setArcHeight(90);
        imageView.setClip(clip);
    }
    private void showDialog(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
    private HashSet<String> getChannels(){
        HashSet<String> channels = new HashSet<>();
        if(channelA.isSelected())channels.add("A");
        if(channelB.isSelected())channels.add("B");
        if(channelC.isSelected())channels.add("C");
        return channels;
    }
    private ArrayList<Post> filteredList(){
        HashSet<String> channels = getChannels();
        ArrayList<Post> filtered = new ArrayList<>();
        for (Post post : allPosts) {
            for (String channel : channels) {
                if(post.getChannelPosted().equals(channel)){
                    filtered.add(post);
                }
            }
        }
        return filtered;
    }
    private void refreshView(){
        Platform.runLater(() -> {
            posts.removeAll(allPosts);
            for (Post post :
                    filteredList()) {
                posts.add(0,post);
            }
            listView.refresh();
        });
    }

    public static void main(String[] args){
        Application.launch(args);
    }
}

