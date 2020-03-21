package com.lassilaiho.calculator.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App extends Application {

    private static Scene scene;
    public static Connection dbConnection;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        dbConnection = DriverManager.getConnection("jdbc:sqlite:calculator.db");
        scene = new Scene(loadFXML("MainView"));
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws SQLException {
        dbConnection.close();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * The main function of the program.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        loadSqliteDriver();
        App.launch();
    }

    private static void loadSqliteDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
