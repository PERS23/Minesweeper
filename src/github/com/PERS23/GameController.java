package github.com.PERS23;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private Minefield mGame;

    private final Image mAliveFaceImage;
    private final Image mDeadFaceImage;
    private final Image mPlacementFaceImage;
    private final Image mFlagImage;
    private final Image mMineImage;

    @FXML private GridPane gameGridDisplay;
    @FXML private HBox gameToolbar;
    @FXML private MenuBar gameMenu;

    public GameController() {
        mGame = Minefield.randomField(Difficulty.BEGINNER);
        mAliveFaceImage = new Image("res/img/aliveFace.png");
        mDeadFaceImage = new Image("res/img/deadFace.png");
        mPlacementFaceImage = new Image("res/img/placementFace.png");
        mFlagImage = new Image("res/img/flag.png");
        mMineImage = new Image("res/img/mine.png");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGameGrid();
    }

    private void initializeGameGrid() {
        for (int y = 0; y < mGame.getHeight(); y++) {
            for (int x = 0; x < mGame.getWidth(); x++) {
                gameGridDisplay.add(createGameGridCell(x, y), x, y);
            }
        }
    }

    private Button createGameGridCell(int x, int y) {
        Button root = new Button("_");
        return root;
    }

    public void refreshGameGrid() {
        for (Node cell : gameGridDisplay.getChildren()) {
            int x = GridPane.getColumnIndex(cell), y = GridPane.getRowIndex(cell);
            Button btn = (Button) cell;
        }
    }
}
