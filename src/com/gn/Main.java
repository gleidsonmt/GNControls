package com.gn;

import com.gn.control.*;
import com.gn.skin.HourSpinner;
import com.gn.skin.TimerContent;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.scenicview.ScenicView;

import java.time.LocalTime;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

//        Locale.setDefault(Locale.TAIWAN);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);

//        Locale.setDefault(Locale.US);

        GNTimePicker days = new GNTimePicker();



        days.setPrefWidth(200);

        MonetaryField moneyField = new MonetaryField();
//        moneyField.setPrefHeight(200);

        root.getChildren().addAll(moneyField, days  );

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

//        ScenicView.show(primaryStage.getScene());
//
//        new DatePicker();
//        new Spinner<>();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
