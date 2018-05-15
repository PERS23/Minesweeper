package github.com.PERS23;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApplication extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GameScreen.fxml"));

        Scene scene = new Scene(root, 1440, 900);
        scene.getStylesheets().addAll("style/buttonStyleSheet.css", "style/outerContainerStyleSheet.css", "style/innerContainerStyleSheet.css");

        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("img/mine.png"));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
