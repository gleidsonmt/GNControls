package io.gleidsonmt.controls.lab;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  29/01/2022
 */
public class NotificationTestAnchor extends Application {

    private final double screenHeight = Screen.getPrimary().getVisualBounds().getHeight() ;
    private final double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    private final double screenMaxX = Screen.getPrimary().getVisualBounds().getMaxX();
    private final double screenMinY = Screen.getPrimary().getVisualBounds().getMinY();

    private final double tileWidth = 250;

    private double offset = 0;

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        TextField input = new TextField("Lorem ipsum dolor color");
        Button btn = new Button("Click to Add");
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.getChildren().addAll(input, btn);
        stage.setScene(new Scene(root, 500, 500));


        Button animation = new Button("Animation");
        root.getChildren().add(animation);

        Stage left = new Stage();
        StackPane leftRoot = new StackPane();
        AnchorPane manageChildren = new AnchorPane();
        leftRoot.getChildren().add(manageChildren);
        Scene scene = new Scene(leftRoot, tileWidth, screenHeight );
        scene.setFill(Color.TRANSPARENT);
        left.setScene(scene);
        left.setX(screenMaxX - tileWidth);
        left.setY(screenMinY);
        left.initStyle(StageStyle.TRANSPARENT);


        stage.show();
        left.initOwner(stage);

        btn.setOnMouseClicked(event -> {
            if (!left.isShowing()) {
                left.show();
            }
            Platform.runLater(() -> {
                createNotification(manageChildren, input.getText());
            });
        });

        animation.setOnMouseClicked(event -> {
            AtomicInteger index = new AtomicInteger();
            AtomicInteger start = new AtomicInteger(60);

            manageChildren.getChildren().forEach( c -> {
//                System.out.println(c);
                TranslateTransition transition = new TranslateTransition();
                transition.setByX(0);
                transition.setToX(tileWidth);
                transition.setDuration(Duration.millis(500));
                transition.setNode(c);
                transition.setDelay(Duration.millis(start.addAndGet(250)));
                transition.play();
                transition.setOnFinished(e -> {
                    manageChildren.getChildren().remove(index.get());
                });
            });
        });
    }

    private void createNotification(AnchorPane manageChildren, String text) {
            // create new and manage
            VBox box = new VBox();
            Label message = new Label(text);
            message.setWrapText(true);
            box.getChildren().add(message);
            box.setStyle("-fx-background-color : red;");
            manageChildren.getChildren().add(box);

            AnchorPane.setLeftAnchor(box, 0D);
            AnchorPane.setRightAnchor(box, 0D);

            manageChildren.getChildren().forEach(c -> {
                offset += ((VBox) c).getHeight();
                AnchorPane.setTopAnchor(box, offset);
            });
            offset = 0;
    }

    private void create(AnchorPane manageChildren) {

    }

    public static void main(String[] args) {
        launch(args);
    }

}
