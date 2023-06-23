package scripts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class FXMLJavaApplication extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLSeleccion.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("Cuatro En Raya");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setMinWidth(600);
        stage.setMinHeight(450);
        stage.setResizable(false);
        stage.show();
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    
}