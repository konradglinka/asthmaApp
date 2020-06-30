import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader myLoader= new FXMLLoader();
        myLoader.setLocation(this.getClass().getResource("/asthmaGUI.fxml"));
        Parent root = myLoader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);
        primaryStage.setTitle("EmailApp");
        primaryStage.setFullScreen(true);
        primaryStage.show();
        primaryStage.setOnCloseRequest(this::handle);



    }

    public static void main(String[] args) {
        launch(args);
    }

    public void handle(WindowEvent event) {
            Platform.exit();
            System.exit(0);
        }
}
