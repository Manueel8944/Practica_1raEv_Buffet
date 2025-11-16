module com.example.appescritorio {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires okhttp3;
    requires com.google.gson;

    opens com.example.appescritorio.com.example.appescritorio to javafx.fxml;
    exports com.example.appescritorio.com.example.appescritorio;
    opens com.example.appescritorio.com.example.appescritorio.clases to com.google.gson;
}