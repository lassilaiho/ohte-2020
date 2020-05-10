package com.lassilaiho.calculator.ui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import com.lassilaiho.calculator.core.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public final class MainViewController {
    private Calculator calculator;

    @FXML
    private ScrollPane historyViewContainer;

    @FXML
    private GridPane historyView;

    @FXML
    private TextField expressionInput;

    @FXML
    private TextField errorDisplay;

    @FXML
    private ListView<String> catalogView;

    @FXML
    private TextField searchInput;

    private ObservableList<String> catalogEntries = FXCollections.observableArrayList();
    private FilteredList<String> filteredCatalogEntries;

    @FXML
    private void initialize() throws SQLException {
        handleUncaughtException(() -> {
            calculator = new Calculator(App.sessionManager.getSession());
            historyView.getChildren().clear();
            for (var entry : calculator.getHistory()) {
                addHistoryEntryRow(entry);
            }
            updateHistoryViewScrollPosition();
            App.updateWindowTitle();
            App.setOnCloseHandler(event -> {
                if (!showConfirmDiscardUnsavedSessionDialog()) {
                    event.consume();
                }
            });
        });
        updateCatalog();
        filteredCatalogEntries = catalogEntries.filtered(x -> true);
        catalogView.setItems(filteredCatalogEntries.sorted());
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredCatalogEntries.setPredicate(x -> x.contains(newValue));
        });
    }

    @FXML
    private void calculate() {
        try {
            var input = expressionInput.getText();
            var value = calculator.calculate(input);
            if (value != null) {
                addHistoryEntryRow(calculator.newestHistoryEntry());
                updateHistoryViewScrollPosition();
                updateCatalog();
            }
            errorDisplay.setText("");
        } catch (CalculatorException exception) {
            errorDisplay.setText(exception.getMessage());
        } catch (Exception exception) {
            showUncaughtErrorAlert(exception);
        }
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
        if (showConfirmDiscardUnsavedSessionDialog()) {
            Platform.exit();
        }
    }

    @FXML
    private void saveSession() {
        handleUncaughtException(() -> {
            var fileChooser = createSessionFileChooser("Save Session File");
            fileChooser.initialFileNameProperty().set("current.session");
            var selectedFile = fileChooser.showSaveDialog(App.scene.getWindow());
            if (selectedFile != null) {
                App.sessionManager.switchDatabase(selectedFile.getAbsolutePath());
                App.updateWindowTitle();
            }
        });
    }

    @FXML
    private void openSession() {
        if (showConfirmDiscardUnsavedSessionDialog()) {
            var selectedFile = createSessionFileChooser("Open Session File")
                .showOpenDialog(App.scene.getWindow());
            if (selectedFile != null) {
                reinitialize(selectedFile.getAbsolutePath());
            }
        }
    }

    @FXML
    private void openDefaultSession() {
        if (showConfirmDiscardUnsavedSessionDialog()) {
            reinitialize(App.defaultSessionFile);
        }
    }

    @FXML
    private void createSession() {
        if (showConfirmDiscardUnsavedSessionDialog()) {
            reinitialize(":memory:");
        }
    }

    private FileChooser createSessionFileChooser(String title) {
        var fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters()
            .add(new ExtensionFilter("Session Files", "*.session"));
        fileChooser.getExtensionFilters().add(new ExtensionFilter("All Files", "*.*"));
        return fileChooser;
    }

    private void reinitialize(String newDatabase) {
        handleUncaughtException(() -> {
            App.sessionManager.openSession(newDatabase);
            initialize();
        });
    }

    private boolean showConfirmDiscardUnsavedSessionDialog() {
        if (App.sessionManager.getCurrentPath() == null) {
            var alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Discard current session");
            alert.setHeaderText(
                "Are you sure you want to discard the current unsaved session?");
            var result = alert.showAndWait();
            return result.isPresent() && result.get() == ButtonType.OK;
        }
        return true;
    }

    private void handleUncaughtException(Action action) {
        try {
            action.run();
        } catch (Exception e) {
            showUncaughtErrorAlert(e);
        }
    }

    private void showUncaughtErrorAlert(Throwable error) {
        var errorText = new TextArea(stackTraceToString(error));
        errorText.setEditable(false);
        errorText.setWrapText(true);

        var alert = new Alert(AlertType.ERROR);
        alert.setTitle("An error occurred");
        alert.setHeaderText("An error occurred");
        alert.setContentText(error.getMessage());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setExpandableContent(errorText);
        alert.show();
    }

    private String stackTraceToString(Throwable error) {
        var writer = new StringWriter();
        error.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    @FunctionalInterface
    private interface Action {
        void run() throws Exception;
    }

    private void updateCatalog() {
        catalogEntries.clear();
        for (var value : calculator.getScope()) {
            catalogEntries.add(value.getName());
        }
    }
}
