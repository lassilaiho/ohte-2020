package com.lassilaiho.calculator.ui;

import java.io.StringReader;
import com.lassilaiho.calculator.core.Calculator;
import com.lassilaiho.calculator.core.EvaluationException;
import com.lassilaiho.calculator.core.lexer.LexerException;
import com.lassilaiho.calculator.core.parser.ParserException;
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
            result.setText(Double.toString(calculator.calculate(reader)));
        } catch (LexerException | ParserException | EvaluationException e) {
            error.setText(e.getMessage());
        } catch (Exception e) {
            var alert = new Alert(AlertType.ERROR);
            alert.setTitle("An error occurred");
            alert.setHeaderText("An error occurred");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    @FXML
    private void clear() {
        expression.setText("");
        result.setText("");
        error.setText("");
    }
}
