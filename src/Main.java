import com.table.HashTableModel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.controller.MainController;

public class Main extends Application{

    @Override
    public void start(Stage stage){
        try {
            FXMLLoader mainLoader= new FXMLLoader(getClass().getResource("./design/main.fxml"));
            Scene scene= new Scene(mainLoader.load(), 800, 600);

            //Create and init the shared model
            HashTableModel model= new HashTableModel();
            MainController mainController= mainLoader.getController();
            mainController.initModel(model);

            String css= this.getClass().getResource("design/style.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setTitle("Visualization of operations on Hash Table");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
