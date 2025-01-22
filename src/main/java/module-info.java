module TaskManagerApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.json;

    // Export the TaskManager package to javafx.graphics
    exports TaskManager to javafx.graphics;

    // Open the controllers package to javafx.fxml for reflection-based access
    opens controllers to javafx.fxml;

    // Open the views package to javafx.fxml (if your FXML files are in this package)
    opens views to javafx.fxml;
}