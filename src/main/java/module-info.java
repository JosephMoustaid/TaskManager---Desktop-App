module TaskManagerApp {
    requires javafx.controls;
    requires javafx.fxml;

    // Open TaskManager and controllers to javafx.fxml for reflection-based access
    opens TaskManager to javafx.fxml;

    // Export TaskManager to javafx.graphics
    exports TaskManager;

    // Optionally open views if you have them
    opens views;
}
