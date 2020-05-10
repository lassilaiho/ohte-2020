package com.lassilaiho.calculator.ui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;
import com.lassilaiho.calculator.persistence.SqliteSessionManager;
import org.apache.commons.cli.*;

/**
 * The main class of the application.
 */
public final class App extends Application {
    public static SqliteSessionManager sessionManager =
        new SqliteSessionManager("jdbc:sqlite");

    public static Scene scene;
    private static Stage stage;

    public static String defaultSessionFile = "default.session";

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        App.stage = stage;
        scene = new Scene(loadFXML("MainView"));
        stage.setScene(scene);
        stage.show();
    }

    public static void updateWindowTitle() {
        var sessionName = sessionManager.getCurrentPath();
        if (sessionName == null) {
            sessionName = "New Session";
        } else if (sessionName.equals(defaultSessionFile)) {
            sessionName = "Default Session";
        }
        stage.setTitle("Calculator - " + sessionName);
    }

    public static void setOnCloseHandler(EventHandler<WindowEvent> handler) {
        stage.setOnCloseRequest(handler);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws Exception {
        try {
            Class.forName("org.sqlite.JDBC");

            var argumentSet = ArgumentSet.parse(args);
            if (argumentSet.getHelp()) {
                argumentSet.printHelp();
                return;
            }
            defaultSessionFile = argumentSet.getDefaultSessionFile();
            sessionManager.openSession(defaultSessionFile);
            App.launch();
        } catch (ParseException | SQLException | IOException
            | UnsupportedOperationException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } finally {
            sessionManager.close();
        }
    }
}
