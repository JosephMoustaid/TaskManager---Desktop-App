package TaskManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class Settings extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Use the correct absolute path to load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/views/settings.fxml"));



            Scene scene = new Scene(root);

            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Set the stage size to the screen size
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());

            stage.setScene(scene);
            stage.setTitle("Task Manager - Settings");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/_51c75ae5-dbc9-4096-a010-a1357c9b9d94-removebg-preview.png")));

            stage.setResizable(true);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
