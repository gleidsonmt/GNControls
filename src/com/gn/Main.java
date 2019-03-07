package com.gn;

import com.gn.test.GNDatePicker;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.scenicview.ScenicView;

import java.time.LocalDate;
import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);

//        Locale.setDefault(Locale.US);

        GNDatePicker days = new GNDatePicker();
        days.setPrefWidth(200);

        new DatePicker();
        root.getChildren().add(days);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        days.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                System.out.println(newValue);
            }
        });

        ScenicView.show(primaryStage.getScene());

    }


    public static void main(String[] args) {
        launch(args);
    }
}
