package com.lassilaiho.calculator.ui;

import java.sql.SQLException;
import com.lassilaiho.calculator.core.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainViewController {
    private Calculator calculator;

    @FXML
    private ScrollPane historyViewContainer;

    @FXML
    private GridPane historyView;

    @FXML
    private TextField expressionInput;

    @FXML
    private Text errorDisplay;

    @FXML
    private void initialize() throws SQLException {
        calculator = new Calculator(App.sessionManager.getSession());
        historyView.getChildren().clear();
        for (var entry : calculator.getHistory()) {
            addHistoryEntryRow(entry);
        }
        updateHistoryViewScrollPosition();
    }

    @FXML
    private void calculate() {
        try {
            var input = expressionInput.getText();
            var value = calculator.calculate(input);
            if (value != null) {
                addHistoryEntryRow(calculator.newestHistoryEntry());
                updateHistoryViewScrollPosition();
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

    private void addHistoryEntryRow(HistoryEntry entry) {
        var expressionLabel = new Label(entry.getExpression());
        expressionLabel.setTextOverrun(OverrunStyle.ELLIPSIS);
        var valueLabel = new Label(entry.getValue().toString());
        valueLabel.setTextOverrun(OverrunStyle.ELLIPSIS);
        historyView.addRow(
            historyView.getRowCount(),
            expressionLabel,
            new Text("="),
            valueLabel);
    }

    private void updateHistoryViewScrollPosition() {
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

    @FXML
    private void exitApplication() {
        Platform.exit();
    }

    @FXML
    private void saveSession() throws Exception {
        var fileChooser = createSessionFileChooser("Save Session File");
        fileChooser.initialFileNameProperty().set("current.session");
        var selectedFile = fileChooser.showOpenDialog(App.scene.getWindow());
        if (selectedFile != null) {
            App.sessionManager.switchDatabase(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void openSession() throws Exception {
        var selectedFile = createSessionFileChooser("Open Session File")
            .showOpenDialog(App.scene.getWindow());
        if (selectedFile != null) {
            reinitialize(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void openDefaultSession() throws Exception {
        reinitialize(App.defaultSessionFile);
    }

    @FXML
    private void createSession() throws Exception {
        reinitialize(":memory:");
    }

    private FileChooser createSessionFileChooser(String title) {
        var fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters()
            .add(new ExtensionFilter("Session Files", "*.session"));
        fileChooser.getExtensionFilters().add(new ExtensionFilter("All Files", "*.*"));
        return fileChooser;
    }

    private void reinitialize(String newDatabase) throws Exception {
        App.sessionManager.openSession(newDatabase);
        initialize();
    }
}
