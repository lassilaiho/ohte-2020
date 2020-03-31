package com.lassilaiho.calculator.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.commons.cli.*;

/**
 * The main class of the application.
 */
public class App extends Application {
    public static Connection dbConnection;

    private static Scene scene;

    private static final String DEFAULT_SESSION_FILE = "calculator.db";

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        scene = new Scene(loadFXML("MainView"));
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws Exception {
        try {
            var options = defineCliOptions();
            var commandLine = new DefaultParser().parse(options, args);
            if (commandLine.hasOption("help")) {
                var formatter = new HelpFormatter();
                formatter.printHelp("calculator [options]", options);
                return;
            }
            var sessionFile =
                commandLine.getOptionValue("default-session", DEFAULT_SESSION_FILE);
            dbConnection = openDatabase(sessionFile);
            Class.forName("org.sqlite.JDBC");
            App.launch();
        } catch (ParseException | SQLException | IOException
            | UnsupportedOperationException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } finally {
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    private static Options defineCliOptions() {
        var options = new Options();
        options.addOption(
            Option.builder().longOpt("default-session").hasArg().type(String.class)
                .desc("path to the default session file").argName("session file")
                .numberOfArgs(1).build());
        options.addOption(
            Option.builder("h").longOpt("help").desc("display usage information")
                .build());
        return options;
    }

    private static Connection openDatabase(String file) throws IOException, SQLException {
        var path = Paths.get(file);
        var parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        return DriverManager.getConnection("jdbc:sqlite:" + path);
    }
}
