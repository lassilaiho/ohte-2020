package com.lassilaiho.calculator.ui;

import com.lassilaiho.calculator.core.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class MainViewController {
    private Calculator calculator = new Calculator();

    @FXML
    private ScrollPane historyViewContainer;

    @FXML
    private GridPane historyView;

    @FXML
    private TextField expressionInput;

    @FXML
    private Text errorDisplay;

    @FXML
    private void calculate() {
        try {
            var input = expressionInput.getText();
            var value = calculator.calculate(input);
            if (value != null) {
                addToHistory(calculator.newestHistoryEntry());
            }
            errorDisplay.setText("");
        } catch (CalculatorException exception) {
            errorDisplay.setText(exception.getMessage());
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

    private void addToHistory(HistoryEntry entry) {
        historyView.addRow(
            calculator.getHistory().size(),
            new Text(entry.getExpression()),
            new Text("="),
            new Text(entry.getValue().toString()));
        historyView.autosize();
        historyViewContainer.setVvalue(historyViewContainer.getVmax());
    }

    @FXML
    private void clearInput() {
        expressionInput.setText("");
        errorDisplay.setText("");
    }

    @FXML
    private void clearHistory() {
        historyView.getChildren().clear();
        calculator.clearHistory();
    }
}
