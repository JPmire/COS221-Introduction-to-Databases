package org.example;

import org.example.dao.EmployeeDAO;
import org.example.model.Employee;
import org.example.util.DatabaseManager;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Load the FXML file you made in Scene Builder
        // Note: Make sure the spelling perfectly matches your file in the resources folder!
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/MainView.fxml"));

        // 2. Give your window a title
        primaryStage.setTitle("Chinook Database App - Task 4");

        // 3. Put your layout into a "Scene" (800x600 is the starting window size)
        primaryStage.setScene(new Scene(root, 1000, 700));

        // 4. Make the window pop up on the screen!
        primaryStage.show();
    }

    public static void main(String[] args) {
        // This launches the JavaFX GUI
        launch(args);
    }
}