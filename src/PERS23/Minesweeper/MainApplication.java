package github.com.PERS23.Minesweeper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class MainApplication extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ResourceBundle resourceBundle = ResourceBundle.getBundle("values/strings", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameScreen.fxml"), resourceBundle);
        Parent root = (Parent) loader.load();

        GameController controller = (GameController) loader.getController();
        controller.setStage(primaryStage);

        Scene scene = new Scene(root);
        scene.getStylesheets().addAll("style/buttonStyleSheet.css", "style/outerContainerStyleSheet.css", "style/innerContainerStyleSheet.css");

        primaryStage.setTitle(resourceBundle.getString("title"));
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("img/mine.png"));
        primaryStage.setWidth(439);
        primaryStage.setMinWidth(439);
        primaryStage.setHeight(595);
        primaryStage.setMinHeight(595);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
