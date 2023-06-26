module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires org.jsoup;
    opens app.demo to javafx.fxml;
    exports app.demo;
}