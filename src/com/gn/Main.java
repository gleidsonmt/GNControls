package com.gn;

import com.gn.test.GNDatePicker;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.scenicview.ScenicView;

import java.time.LocalDate;
import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

//        Locale.setDefault(Locale.TAIWAN);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);

//        Locale.setDefault(Locale.US);

        GNDatePicker days = new GNDatePicker();


        days.setPrefWidth(200);

        root.getChildren().addAll(days, new Button("ok"));

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        ScenicView.show(primaryStage.getScene());

        new DatePicker();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
