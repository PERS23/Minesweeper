package github.com.PERS23;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private Difficulty mCurrentDifficulty;
    private Minefield mGame;
    private GameStats mGameStats;

    private Timeline mTimerAnimation;
    private boolean mTimerRunning;
    private int mCurrentCount;

    private final Image mAliveFaceImage;
    private final Image mDeadFaceImage;
    private final Image mPlacementFaceImage;
    private final Image mVictoryFaceImage;
    private final Image mFlagImage;
    private final Image mMineImage;
    private final Image mWrongMineImage;

    private final Font mGridFont;
    private final Font mTimerFont;
    private final Background mTimerBackground;
    private final Paint mTimerColor;
    private final Paint[] mAdjacentColors;

    private Stage mStage;

    @FXML private CheckMenuItem beginnerOption;
    @FXML private CheckMenuItem intermediateOption;
    @FXML private CheckMenuItem expertOption;
    @FXML private Label flagCountDisplay;
    @FXML private Label timerDisplay;
    @FXML private VBox gameContainer;
    @FXML private Button gameStatusFace;
    @FXML private GridPane gameGridDisplay;

    public GameController() {
        mCurrentDifficulty = Difficulty.BEGINNER;
        mGame = Minefield.randomField(mCurrentDifficulty);
        loadInStats();

        mAliveFaceImage = new Image("img/aliveFace.png");
        mDeadFaceImage = new Image("img/deadFace.png");
        mPlacementFaceImage = new Image("img/placementFace.png");
        mVictoryFaceImage = new Image("img/victoryFace.png");
        mFlagImage = new Image("img/flag.png");
        mMineImage = new Image("img/mine.png");
        mWrongMineImage = new Image("img/wrongMine.png");

        mGridFont = new Font("Visitor TT1 BRK", 20);

        mTimerFont = new Font("Digital-7", 48);
        mTimerBackground = new Background(new BackgroundFill(Paint.valueOf("#000000"), CornerRadii.EMPTY, Insets.EMPTY));
        mTimerColor = Paint.valueOf("#FF180B");

        mAdjacentColors = new Paint[] { Paint.valueOf("#0000FF"), Paint.valueOf("#007B00"), Paint.valueOf("#FF0000"),
            Paint.valueOf("#00007B"), Paint.valueOf("#7B0000"), Paint.valueOf("#007B7B"), Paint.valueOf("#000000"),
            Paint.valueOf("#7B7B7B") };
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGameGrid();

        flagCountDisplay.setText(String.format("%03d", mGame.getFlagsLeft()));
        flagCountDisplay.setFont(mTimerFont);
        flagCountDisplay.setTextFill(mTimerColor);
        flagCountDisplay.setBackground(mTimerBackground);

        mTimerAnimation = new Timeline(new KeyFrame(Duration.seconds(1), e -> this.incrementTimer()));
        mTimerAnimation.setCycleCount(Animation.INDEFINITE);

        timerDisplay.setText("000");
        timerDisplay.setFont(mTimerFont);
        timerDisplay.setTextFill(mTimerColor);
        timerDisplay.setBackground(mTimerBackground);

        gameStatusFace.setGraphic(new ImageView(mAliveFaceImage));
    }

    private void loadInStats() {
        File statsSave = new File(GameStats.SAVE_FILE);

        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(statsSave))) {
            mGameStats = (GameStats) is.readObject();
        } catch (FileNotFoundException e) {
            mGameStats = new GameStats();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        mStage = stage;
    }

    @FXML
    private void startNewGame() {
        mGame = Minefield.randomField(mCurrentDifficulty);

        gameGridDisplay.getChildren().clear();
        initializeGameGrid();

        determineStatusFace();
        updateFlagCount();
        resetTimer();

        updateStageSize();
    }

    private void updateStageSize() {
        /* You have to add the components before the stage is shown and get the height/width
         * values after the stage is shown.
         *
         * Real dumb way of doing min size.
         */
        switch (mCurrentDifficulty) {
            case BEGINNER:
                mStage.setMinWidth(439);
                mStage.setWidth(439);
                mStage.setMinHeight(595);
                mStage.setHeight(595);
                break;
            case INTERMEDIATE:
                mStage.setMinWidth(733);
                mStage.setWidth(733);
                mStage.setMinHeight(888);
                mStage.setHeight(888);
                break;
            case EXPERT:
                mStage.setMinWidth(1321);
                mStage.setWidth(1321);
                mStage.setMinHeight(888);
                mStage.setHeight(888);
                break;
        }
    }

    @FXML
    public void handleChangeDifficulty(ActionEvent e) {
        beginnerOption.setSelected(false);
        intermediateOption.setSelected(false);
        expertOption.setSelected(false);

        if (e.getTarget() == beginnerOption) {
            changeDifficulty(Difficulty.BEGINNER);
            beginnerOption.setSelected(true);
        } else if (e.getTarget() == intermediateOption) {
            changeDifficulty(Difficulty.INTERMEDIATE);
            intermediateOption.setSelected(true);
        } else {
            changeDifficulty(Difficulty.EXPERT);
            expertOption.setSelected(true);
        }
    }

    private void changeDifficulty(Difficulty difficultyLevel) {
        mCurrentDifficulty = difficultyLevel;
    }

    @FXML
    public void showStats() {
        boolean isTimerRunning = mTimerRunning;

        if (isTimerRunning) {
            stopTimer();
        }

        Alert statistics = new Alert(Alert.AlertType.NONE);

        statistics.setTitle("Fastest Mine Sweepers");
        statistics.setContentText(mGameStats.toString());

        Stage alertStage = (Stage) statistics.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(mMineImage);

        ButtonType reset = new ButtonType("Reset Scores");
        ButtonType ok = new ButtonType("Ok");

        statistics.getButtonTypes().setAll(reset, ok);

        Optional<ButtonType> result = statistics.showAndWait();

        if (result.get() == reset) {
            mGameStats.resetStats();
        }

        if (isTimerRunning) {
            startTimer();
        }
    }

    @FXML
    public void handleExitMenuOption() {
        Platform.exit();
    }

    private void initializeGameGrid() {
        for (int y = 0; y < mGame.getHeight(); y++) {
            for (int x = 0; x < mGame.getWidth(); x++) {
                gameGridDisplay.add(createGameGridCell(x, y), x, y);
            }
        }
    }

    private Button createGameGridCell(int x, int y) {
        Button root = new Button("");

        root.setFont(mGridFont);
        root.getStyleClass().add("covered_button");
        root.setMinSize(42, 42);
        root.setMaxSize(42, 42);

        root.setOnMousePressed(e -> {
            MouseButton button = e.getButton();
            if (button == MouseButton.PRIMARY && mGame.isCovered(x, y)) {
                setPlacementFaceImage();
            }
        });

        root.setOnMouseReleased(e -> { // Putting action code in release so face gets updated after
            MouseButton button = e.getButton();

            if (button == MouseButton.PRIMARY && mGame.isCovered(x, y)) {
                mGame.uncover(x, y);

                if (!mTimerRunning) {
                    startTimer();
                }

                if (mGame.getVictoryStatus() == VictoryStatus.PENDING) {
                    refreshGameGrid();
                } else if (mGame.getVictoryStatus() == VictoryStatus.SUCCESS) {
                    refreshGameGrid();
                    disableAllCells();
                    stopTimer();
                    mGameStats.update(mCurrentCount, mCurrentDifficulty);
                } else {
                    highlightAllMines();
                    highlightDeathMine(x, y);
                    disableAllCells();
                    stopTimer();
                }

                determineStatusFace();
            } else if (button == MouseButton.SECONDARY && mGame.isCovered(x, y)) {
                mGame.toggleFlag(x, y);
                refreshGameGrid();
                updateFlagCount();
            }
        });

        return root;
    }

    private void highlightDeathMine(int x, int y) {
        for (Node cell : gameGridDisplay.getChildren()) {
            if (GridPane.getColumnIndex(cell) == x && GridPane.getRowIndex(cell) == y) {
                Button death = (Button) cell;
                death.getStyleClass().removeAll("covered_button");
                death.getStyleClass().add("death_button");
                break;
            }
        }
    }

    private void highlightAllMines() {
        for (Node cell : gameGridDisplay.getChildren()) {
            int x = GridPane.getColumnIndex(cell), y = GridPane.getRowIndex(cell);
            Button btn = (Button) cell;

            if (mGame.isMine(x, y) && !mGame.isFlagged(x, y)) {
                btn.setGraphic(new ImageView(mMineImage));
                btn.getStyleClass().removeAll("covered_button");
                btn.getStyleClass().add("uncovered_button");
            } else if (!mGame.isMine(x, y) && mGame.isFlagged(x, y)) {
                btn.setGraphic(new ImageView(mWrongMineImage));
                btn.getStyleClass().removeAll("covered_button");
                btn.getStyleClass().add("uncovered_button");
            }
        }
    }

    private void determineStatusFace() {
        if (mGame.getVictoryStatus() == VictoryStatus.SUCCESS) {
            setSuccessFace();
        } else if (mGame.getVictoryStatus() == VictoryStatus.PENDING) {
            setAliveFace();
        } else {
            setDeadFace();
        }
    }

    private void setAliveFace() {
        gameStatusFace.setGraphic(new ImageView(mAliveFaceImage));
    }

    private void setDeadFace() {
        gameStatusFace.setGraphic(new ImageView(mDeadFaceImage));
    }

    private void setSuccessFace() {
        gameStatusFace.setGraphic(new ImageView(mVictoryFaceImage));
    }

    private void setPlacementFaceImage() {
        gameStatusFace.setGraphic(new ImageView(mPlacementFaceImage));
    }

    private void refreshGameGrid() {
        for (Node cell : gameGridDisplay.getChildren()) {
            int x = GridPane.getColumnIndex(cell), y = GridPane.getRowIndex(cell);
            Button btn = (Button) cell;

            if (mGame.isCovered(x, y)) {
                btn.getStyleClass().add("covered_button");
                if (mGame.isFlagged(x, y)) {
                    ImageView flag = new ImageView(mFlagImage);
                    btn.setGraphic(flag);
                } else {
                    btn.setGraphic(null);
                }
            } else {
                if (btn.getStyleClass().removeAll("covered_button")) {
                    btn.getStyleClass().add("uncovered_button");

                    int adjacentCount = mGame.getAdjacentMineCount(x, y);
                    if (adjacentCount > 0) {
                        btn.setText(Integer.toString(adjacentCount));
                        btn.setTextFill(mAdjacentColors[adjacentCount - 1]);
                    }
                }
            }
        }
    }

    private void startTimer() {
        mTimerAnimation.play();
        mTimerRunning = true;
    }

    private void stopTimer() {
        mTimerAnimation.stop();
        mTimerRunning = false;
    }

    private void resetTimer() {
        stopTimer();
        mCurrentCount = 0;
        timerDisplay.setText(String.format("%03d", mCurrentCount));
    }

    private void incrementTimer() {
        ++mCurrentCount;
        if (mCurrentCount < 1000) {
            timerDisplay.setText(String.format("%03d", mCurrentCount));
        }
    }

    private void updateFlagCount() {
        flagCountDisplay.setText(String.format("%03d", mGame.getFlagsLeft()));
    }

    private void disableAllCells() {
        for (Node cell : gameGridDisplay.getChildren()) {
            Button btn = (Button) cell;
            btn.setDisable(true);
        }
    }
}
