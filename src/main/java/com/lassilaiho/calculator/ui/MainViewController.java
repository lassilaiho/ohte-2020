package com.lassilaiho.calculator.ui;

import java.io.StringReader;
import com.lassilaiho.calculator.core.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

public class MainViewController {
    private Calculator calculator = new Calculator();

    @FXML
    private TextField expression;

    @FXML
    private TextField result;

    @FXML
    private Text error;

    @FXML
    private void calculate() {
        try {
            var reader = new StringReader(expression.getText());
            var value = calculator.calculate(reader);
            result.setText(value == null ? "" : value.toString());
            error.setText("");
        } catch (CalculatorException exception) {
            error.setText(exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            showErrorAlert(exception.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        var alert = new Alert(AlertType.ERROR);
        alert.setTitle("An error occurred");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void clear() {
        expression.setText("");
        result.setText("");
        error.setText("");
    }
}
