package com.gn;

import com.gn.lab.GNCalendarTile;
import com.gn.lab.GNStyledCalendar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.scenicview.ScenicView;

import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = new VBox();

        GNCalendarTile days = new GNCalendarTile();

        new DatePicker();

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(days, 300, 275));
        primaryStage.show();

//        ScenicView.show(primaryStage.getScene());

    }


    public static void main(String[] args) {
        launch(args);
    }
}
