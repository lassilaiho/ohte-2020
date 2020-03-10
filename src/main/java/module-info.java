module com.lassilaiho.calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.lassilaiho.calculator.ui to javafx.fxml;

    exports com.lassilaiho.calculator.ui;
}
