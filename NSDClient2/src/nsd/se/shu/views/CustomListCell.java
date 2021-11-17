package nsd.se.shu.views;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import nsd.se.shu.controllers.ClientViewController;
import nsd.se.shu.models.Message;
import nsd.se.shu.utils.Util;

import java.io.IOException;

public class CustomListCell extends ListCell<Message> {

    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);

        if(empty || message == null){
            setText(null);
            setGraphic(null);
        } else {

            Label text = new Label();
            text.setTextFill(Color.web("#DFE7F2"));
            VBox vBox = new VBox();
            vBox.setSpacing(5);
            setGraphic(vBox);

            if(ClientViewController.userNameField.getText().equals(message.getNickName())){
                text.setStyle("-fx-background-color: #4A5B8C;" +
                        "-fx-border-radius: 5 5 5 5;\n" +
                        "-fx-background-radius: 5 5 5 5;");
                text.setText("  " + message.getMessage() + "  : Me" + "  ");
                text.setAlignment(Pos.CENTER_RIGHT);
                vBox.setAlignment(Pos.CENTER_RIGHT);

            } else {
                text.setStyle("-fx-background-color: #A66F3F;" +
                        "-fx-border-radius: 5 5 5 5;\n" +
                        "    -fx-background-radius: 5 5 5 5");
                text.setText("  " + message.getNickName() + ": " + message.getMessage() + "  ");
                text.setTextAlignment(TextAlignment.LEFT);
                vBox.setAlignment(Pos.CENTER_LEFT);
            }

            if(message.getMessage().length() > 0){
                vBox.getChildren().add(text);
            }

            if(message.getImage() != null){
                Image image = null;
                try {
                    image = SwingFXUtils.toFXImage(Util.base64ToIamge(message.getImage()), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ImageView imageView = new ImageView(image);
                imageView.setSmooth(true);
                imageView.setPreserveRatio(true);
                vBox.getChildren().add(imageView);
                if(ClientViewController.userNameField.getText().equals(message.getNickName())){
                    vBox.setAlignment(Pos.CENTER_RIGHT);
                }else{
                    vBox.setAlignment(Pos.CENTER_LEFT);
                }
            }
        }

    }
}
