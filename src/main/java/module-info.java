module com.lassilaiho.calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;
    requires transitive java.sql.rowset;
    requires sqlite.jdbc;

    opens com.lassilaiho.calculator.ui to javafx.fxml;

    exports com.lassilaiho.calculator.ui;
    exports com.lassilaiho.calculator.core;
    exports com.lassilaiho.calculator.core.lexer;
    exports com.lassilaiho.calculator.core.parser;
    exports com.lassilaiho.calculator.persistence;
}
