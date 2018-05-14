package github.com.PERS23;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private Difficulty mCurrentDifficulty;
    private Minefield mGame;

    private final Image mAliveFaceImage;
    private final Image mDeadFaceImage;
    private final Image mPlacementFaceImage;
    private final Image mVictoryFaceImage;
    private final Image mFlagImage;
    private final Image mMineImage;

    private final Font mGridFont;

    private final Font mTimerFont;
    private final Background mTimerBackground;
    private final Paint mTimerColor;

    @FXML private MenuBar gameMenu;
    @FXML private VBox gameContainer;
    @FXML private GridPane gameToolbar;
    @FXML private Label flagCountDisplay;
    @FXML private Label timerDisplay;
    @FXML private Button gameStatusFace;
    @FXML private GridPane gameGridDisplay;

    public GameController() {
        mCurrentDifficulty = Difficulty.BEGINNER;
        mGame = Minefield.randomField(mCurrentDifficulty);

        mAliveFaceImage = new Image("img/aliveFace.png");
        mDeadFaceImage = new Image("img/deadFace.png");
        mPlacementFaceImage = new Image("img/placementFace.png");
        mVictoryFaceImage = new Image("img/victoryFace.png");
        mFlagImage = new Image("img/flag.png");
        mMineImage = new Image("img/mine.png");

        mGridFont = new Font("Visitor TT1 BRK", 20);

        mTimerFont = new Font("Digital-7", 48);
        mTimerBackground = new Background(new BackgroundFill(Paint.valueOf("#000000"), CornerRadii.EMPTY, Insets.EMPTY));
        mTimerColor = Paint.valueOf("#FF180B");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGameGrid();

        gameContainer.setMaxWidth(gameGridDisplay.getWidth());

        flagCountDisplay.setText("0" + mCurrentDifficulty.numOfMines());
        flagCountDisplay.setFont(mTimerFont);
        flagCountDisplay.setTextFill(mTimerColor);
        flagCountDisplay.setBackground(mTimerBackground);

        timerDisplay.setText("000");
        timerDisplay.setFont(mTimerFont);
        timerDisplay.setTextFill(mTimerColor);
        timerDisplay.setBackground(mTimerBackground);

        gameStatusFace.setGraphic(new ImageView(mAliveFaceImage));
    }

    private void initializeGameGrid() {
        for (int y = 0; y < mGame.getHeight(); y++) {
            for (int x = 0; x < mGame.getWidth(); x++) {
                gameGridDisplay.add(createGameGridCell(x, y), x, y);
            }
        }
    }

    private Button createGameGridCell(int x, int y) {
        Button root = new Button("3");
        root.setFont(mGridFont);

        /*ImageView mineGraphic = new ImageView();
        mineGraphic.setImage(mMineImage);
        mineGraphic.setFitWidth(30);
        mineGraphic.setFitHeight(30);

        root.setGraphic(mineGraphic); */
        return root;
    }

    public void refreshGameGrid() {
        for (Node cell : gameGridDisplay.getChildren()) {
            int x = GridPane.getColumnIndex(cell), y = GridPane.getRowIndex(cell);
            Button btn = (Button) cell;
        }
    }
}
