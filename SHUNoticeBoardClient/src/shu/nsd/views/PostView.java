package shu.nsd.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import shu.nsd.models.Post;
import shu.nsd.utils.Util;

public class PostView extends ListCell<Post> {

    @Override
    protected void updateItem(Post post, boolean empty) {
        super.updateItem(post, empty);
        if(!empty){

            setGraphic(null);

            ImageView profileImageView = new ImageView(Util.base64ToIamge(post.getProfileImage()));
            profileImageView.setFitWidth(40);
            profileImageView.setFitHeight(40);
            Rectangle clip = new Rectangle(profileImageView.getFitWidth(), profileImageView.getFitHeight());
            clip.setArcWidth(90);
            clip.setArcHeight(90);
            profileImageView.setClip(clip);

            Label name = new Label(post.getUsername());
            name.setTextFill(Color.color(1,1,1));
            name.setPrefWidth(180);
            name.setFont(new Font("Arial", 20));

            Label created = new Label(post.getPostedDate());
            created.setTextFill(Color.color(1,1,1));
            created.setPrefWidth(180);
            created.setFont(new Font("Arial", 16));
            created.setTextAlignment(TextAlignment.RIGHT);

            VBox lables = new VBox(name, created);

            HBox profileGroup = new HBox(profileImageView, lables);
            profileGroup.setSpacing(10);
            profileGroup.setPadding(new Insets(5));
            profileGroup.setAlignment(Pos.CENTER_LEFT);

            Label title = new Label(post.getPostedTitle());
            title.setTextFill(Color.color(1,1,1));
            title.setTextAlignment(TextAlignment.LEFT);
            title.setMaxWidth(200);
            title.setMaxWidth(200);
            title.setFont(new Font("Arial", 24));
            title.setWrapText(true);

            Label message = new Label(post.getPostedMessage());
            message.setTextFill(Color.color(1,1,1));
            message.setWrapText(true);
            message.setMaxWidth(200);

            VBox postGourp;
            if(post.getPostedImage() != null){
                ImageView postedImage = new ImageView(Util.base64ToIamge(post.getPostedImage()));
                postedImage.setFitHeight(200);
                postedImage.setFitWidth(200);
                postGourp = new VBox(profileGroup, title, message, postedImage);
            }else{
                postGourp = new VBox(profileGroup, title, message);
            }

            postGourp.setSpacing(7);
            postGourp.setAlignment(Pos.CENTER);
            postGourp.setPadding(new Insets(5));
            String cssLayout = "-fx-border-color: cyan;\n" +
                    "-fx-border-insets: 5;\n" +
                    "-fx-border-width: 3;\n" +
                    "-fx-border-style: dashed;\n";
            postGourp.setStyle(cssLayout);

            setGraphic(postGourp);

        }else{
            setGraphic(null);
        }
    }
}
