package io.gleidsonmt.controls.lab;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  29/01/2022
 */
public class NotificationTest extends Application {


    private Stage notificationStage;

    private Pane contentPane;

    private static final Integer NOTIFICATION_WIDTH = 250;

    private Double notificationOffset = 0.0;

    private static final Integer SPACING_BETWEEN_NOTIFICATIONS = 15;

    @Override
    public void start(Stage stage) throws Exception {
        Stage mainStage = new Stage();
        TextField textField = new TextField("Some long text for testing purpose with even more letters in oder to create at least one linebreak...");
        Button button = new Button("Add Notification");
        button.setOnAction(actionEvent -> {
            addNotification(textField.getText());
        });

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(textField, button);
        mainStage.setScene(new Scene(vBox, 300, 300));
        mainStage.show();
    }

    private void addNotification(String text) {

        if(notificationStage == null) {
            notificationStage = new Stage();
            notificationStage.setWidth(NOTIFICATION_WIDTH);
            notificationStage.setHeight(Screen.getPrimary().getVisualBounds().getHeight() - 50);
            notificationStage.setX(Screen.getPrimary().getVisualBounds().getWidth() - 260);
            notificationStage.setY(50);
            contentPane = new Pane();
            contentPane.setStyle("-fx-background-color: transparent");
            notificationStage.setScene(new Scene(contentPane));
            notificationStage.initStyle(StageStyle.TRANSPARENT);
            notificationStage.getScene().setFill(Color.TRANSPARENT);
            notificationStage.show();
        }

        VBox notificationBox = new VBox();

        notificationBox.setMaxWidth(NOTIFICATION_WIDTH);
        notificationBox.setMinWidth(NOTIFICATION_WIDTH);
        notificationBox.setStyle("-fx-background-radius: 10; -fx-background-color: red");
        notificationBox.getChildren().add(new Label("Title of Notification"));

        Label message = new Label(text);
        message.setWrapText(true);

        notificationBox.getChildren().add(message);
        notificationBox.setLayoutY(notificationOffset);

        contentPane.getChildren().add(notificationBox);

        // Needs to be done - otherwise the height would be 0
        contentPane.applyCss();
        contentPane.layout();

//        notificationOffset += notificationBox.getHeight() + SPACING_BETWEEN_NOTIFICATIONS + 15;
        Platform.runLater(() -> {
            System.out.println("notificationOffset = " + notificationBox.getHeight());

                notificationOffset += notificationBox.getHeight() ;
        });

//        notificationOffset = 0D;

    }

    public static void main(String[] args) {
        launch(args);
    }

}
