
module io.github.gleidsonmt.gncontrols {

    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires java.logging;

    requires scenicView;
    requires controlsfx;

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

}