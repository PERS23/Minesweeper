package github.com.PERS23;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApplication extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameScreen.fxml"));
        Parent root = (Parent) loader.load();

        GameController controller = (GameController) loader.getController();
        controller.setStage(primaryStage);

        Scene scene = new Scene(root);
        scene.getStylesheets().addAll("style/buttonStyleSheet.css", "style/outerContainerStyleSheet.css", "style/innerContainerStyleSheet.css");

        primaryStage.setTitle("Minesweeper");
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
