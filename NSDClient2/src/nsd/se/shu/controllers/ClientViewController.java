package nsd.se.shu.controllers;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nsd.se.shu.services.RequestHandler;
import nsd.se.shu.models.Message;
import nsd.se.shu.utils.Util;
import nsd.se.shu.views.CustomListCell;
import java.io.*;
import java.net.Socket;


public class ClientViewController extends Application {

    public static final ObservableList<Message> messages = FXCollections.observableArrayList();
    public static TextField userNameField;
    public static String selectedImage = null;

    private RequestHandler socketManager;
    private Socket socket;

    private File selectedFile;
    private FileChooser fileChooser;
    private Stage stage;
    private Label fileSelectedLabel;
    private Label statusLabel;
    private ListView<Message> listView;

    private TextField ipField;
    private TextField portField;
    private TextField chatField;

    private Button connectBtn;
    private Button sendBtn;
    private Button fileChooseBtn;

    private EventHandler<ActionEvent> connectEvent;
    private EventHandler<ActionEvent> sendEvent;
    private EventHandler<ActionEvent> fileChooseEvent;

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;

        setEvents();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(5));
        root.setBottom(setBottom());
        root.setCenter(setCenter());
        root.setTop(setTop());

        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("SHU Chat box");
        stage.setScene(scene);
        stage.setMaxHeight(500);
        stage.setMinHeight(500);
        stage.setMaxWidth(450);
        stage.setMinWidth(450);
        stage.show();
    }

    public void setEvents() {
        connectEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (connectBtn.getText().equals("Connect")) {
                    try {
                        socket = new Socket(ipField.getText(), Integer.parseInt(portField.getText()));
                        socketManager = new RequestHandler(socket, (message) -> {
                            Platform.runLater(() -> {
                                messages.add(message);
                                listView.scrollTo(listView.getItems().size());
                            });
                        });

                        if (socketManager.isConnected()) {
                            statusLabel.setText("Connected");
                            connectBtn.setText("Exit");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    socketManager.closeSocket();
                    if (!socketManager.isConnected()) {
                        statusLabel.setText("Not connected");
                        connectBtn.setText("Connect");
                    }
                }
            }
        };

        sendEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (userNameField.getText().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Please Enter User Name");
                    alert.showAndWait();
                    return;
                }
                if (socketManager != null && socketManager.isConnected()) {
                    if(chatField.getText().length() == 0 && selectedImage == null) return;
                    Message message = new Message(chatField.getText(), userNameField.getText(), selectedImage);
                    socketManager.outputStream(message);
                    selectedImage = null;
                    fileSelectedLabel.setText("File is not selected");
                    chatField.setText("");
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Not connected");
                    alert.showAndWait();
                    statusLabel.setText("Not connected");
                }
            }
        };

        fileChooseEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                fileChooser = new FileChooser();
                selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    fileSelectedLabel.setText(selectedFile.getName());
                    System.out.println(selectedFile.getPath());
                    try {
                        selectedImage = Util.imageToBase64(selectedFile.getPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    fileSelectedLabel.setText("File is not selected");
                }
            }
        };
    }

    public HBox setTop() {

        userNameField = new TextField();
        userNameField.setPrefWidth(150);
        userNameField.setPromptText("Pelase enter user name");

        ipField = new TextField("127.0.0.1");
        ipField.setPrefWidth(100);
        ipField.setPromptText("ip address");
        ipField.setDisable(true);

        portField = new TextField("12345");
        portField.setPrefWidth(50);
        portField.setPromptText("port");
        portField.setDisable(true);

        HBox hBox = new HBox(userNameField, ipField, portField);
        hBox.setSpacing(7);
        hBox.setAlignment(Pos.BASELINE_CENTER);
        hBox.setHgrow(userNameField, Priority.ALWAYS);
        hBox.setHgrow(ipField, Priority.ALWAYS);
        hBox.setHgrow(portField, Priority.ALWAYS);
        hBox.setFillHeight(true);

        return hBox;
    }

    public HBox setBottom() {
        connectBtn = new Button("Connect");
        connectBtn.setPrefWidth(70);
        connectBtn.setOnAction(connectEvent);


        sendBtn = new Button("Send");
        sendBtn.setPrefWidth(70);
        sendBtn.setOnAction(sendEvent);

        fileChooseBtn = new Button("Choose");
        fileChooseBtn.setPrefWidth(70);
        fileChooseBtn.setOnAction(fileChooseEvent);

        chatField = new TextField();
        chatField.setPrefWidth(350);
        chatField.setPromptText("Write something..");

        fileSelectedLabel = new Label("File is not selected");
        fileSelectedLabel.setPrefWidth(350);

        statusLabel = new Label("Not connected");
        statusLabel.setPrefWidth(350);

        VBox btns = new VBox(sendBtn, fileChooseBtn, connectBtn);
        VBox fields = new VBox(chatField, fileSelectedLabel, statusLabel);
        fields.setSpacing(7);

        HBox hBox = new HBox(fields, btns);
        hBox.setSpacing(7);
        hBox.setAlignment(Pos.BASELINE_CENTER);
        hBox.setHgrow(connectBtn, Priority.ALWAYS);
        hBox.setHgrow(chatField, Priority.ALWAYS);
        hBox.setHgrow(sendBtn, Priority.ALWAYS);
        hBox.setHgrow(fileChooseBtn, Priority.ALWAYS);

        hBox.setFillHeight(true);

        return hBox;
    }

    public ListView<Message> setCenter() {
        listView = new ListView<>(messages);
        listView.setCellFactory(listView -> new CustomListCell());
        listView.setStyle("-fx-control-inner-background: #8697A6;");
        return listView;
    }

    public static void main(String[] args) {
        launch(args);
    }
}