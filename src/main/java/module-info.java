
module io.github.gleidsonmt.gncontrols {

    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires java.logging;

//    requires scenicView;
//    requires fr.brouillard.oss.cssfx;
//    requires controlsfx;
    

    opens io.github.gleidsonmt.gncontrols to javafx.controls, javafx.fxml;
    opens io.github.gleidsonmt.gncontrols.material.icon to javafx.fxml;
    opens io.github.gleidsonmt.gncontrols.material.color to javafx.fxml;

    exports io.github.gleidsonmt.gncontrols;

    exports io.github.gleidsonmt.gncontrols.test;
    exports io.github.gleidsonmt.gncontrols.options;
    exports io.github.gleidsonmt.gncontrols.material.icon;
    exports io.github.gleidsonmt.gncontrols.options.model;

    opens io.github.gleidsonmt.gncontrols.skin to javafx.controls, javafx.fxml;
    opens io.github.gleidsonmt.gncontrols.options to javafx.controls, javafx.fxml;
    opens io.github.gleidsonmt.gncontrols.skin.button to javafx.controls, javafx.fxml;

    exports io.github.gleidsonmt.gncontrols.skin;
    exports io.github.gleidsonmt.gncontrols.show;
    exports io.github.gleidsonmt.gncontrols.enums;
    exports io.github.gleidsonmt.gncontrols.controls.status;
    exports io.github.gleidsonmt.gncontrols.controls;

    opens io.github.gleidsonmt.gncontrols.controls to javafx.controls, javafx.fxml;

    exports io.github.gleidsonmt.gncontrols.controls.text_box;
    exports io.github.gleidsonmt.gncontrols.behavior;

    opens io.github.gleidsonmt.gncontrols.controls.text_box to javafx.controls, javafx.fxml;
    exports io.github.gleidsonmt.gncontrols.controls.skin;
    opens io.github.gleidsonmt.gncontrols.controls.skin to javafx.controls, javafx.fxml;

    opens io.github.gleidsonmt.gncontrols.behavior to javafx.controls, javafx.fxml, javafx.base;
    opens io.github.gleidsonmt.gncontrols.test.controllers to javafx.controls, javafx.fxml, javafx.base;


}