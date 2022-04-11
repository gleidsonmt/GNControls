package io.gleidsonmt.controls;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * A simple init with autocomplete combo box
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setEditable(true);

        // The list
        FilteredList<String> fullList = new FilteredList<>(FXCollections.<String>observableArrayList(
                "Jorge",
                "Luiza", "Paulo", "Jorge", "Andre"),
                p -> true
        );

        comboBox.setItems(fullList);

        root.getChildren().addAll(comboBox);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));

//        comboBox.set

        primaryStage.show();
//
//        final ListView[] _listView = {null}; // it is necessary...
//
//
//        comboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
//            if(_listView[0] == null) {
//                _listView[0] = (ListView) comboBox.lookup(".list-view"); // getting a listview from combo..
//                _listView[0].setFixedCellSize(30);
//            }
//        });
//
//        comboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                fullList.setPredicate(professional -> professional.toLowerCase().contains(newValue.toLowerCase()));
//                _listView[0].resize(
//                        comboBox.getWidth(),
//                        (fullList.size() * 30) + // number of items.. sum list padding
//                                (_listView[0].getPadding().getTop() + _listView[0].getPadding().getBottom()));
//                if(fullList.size() == 0){
//                    comboBox.hide();
//                } else {
//                    comboBox.show();
//                }
//            }
//        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}
