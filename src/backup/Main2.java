package io.gleidsonmt.controls;

import com.gn.control.GNComboFilter;
import com.gn.control.GNDatePicker;
import com.gn.control.GNMonetaryField;
import com.gn.control.GNTimePicker;
import com.gn.lab.CalendarPane;
import com.gn.lab.CalendarSkin;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
//import org.scenicview.ScenicView;
//import org.scenicview.ScenicView;

import java.time.LocalTime;
import java.util.Locale;

public class Main2 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/com/gn/testFxml.fxml"));
//
        Locale.setDefault(Locale.CANADA);


//        primaryStage.getScene().getStylesheets().add(Main2.class.getResource("/com/gn/calendar.css").toExternalForm());


        GNDatePicker timePicker = new GNDatePicker();
//        Button btn = new Button("Clear");
//        btn.setOnMouseClicked(event -> {
//            System.out.println("clear");
//            timePicker.setTime(LocalTime.MIN);
//        });



        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(timePicker, 300, 275));

        primaryStage.show();

//        ScenicView.show(primaryStage.getScene());

    }


    public static void main(String[] args) {
        launch(args);
    }
}
