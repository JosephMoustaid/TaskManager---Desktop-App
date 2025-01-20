package models;

import java.io.*;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TaskManager {

    public TaskManager() {

    }

    public void saveBoard(Board board, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(board);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void openAddTaskDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/addTaskDialog.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Task");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Board loadBoard(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Board) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addBoard(Board board, Set<Board> boards) {
        boards.add(board);
    }
}