module com.lassilaiho.calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.lassilaiho.calculator.ui to javafx.fxml;

    exports com.lassilaiho.calculator.ui;
    exports com.lassilaiho.calculator.core;
    exports com.lassilaiho.calculator.core.lexer;
    exports com.lassilaiho.calculator.core.parser;
}
