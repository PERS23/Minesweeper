package github.com.PERS23;

public enum Difficulty {
    BEGINNER("Beginner", 10, 9, 9, 0),
    INTERMEDIATE("Intermediate", 40, 16, 16, 1),
    EXPERT("Expert", 99, 30, 16, 2);

    private String mDifficultyName;
    private int mNumOfMines;
    private int mWidth;
    private int mHeight;
    private int mHighscoreIndex;

    private Difficulty(String difficultyName, int numOfMines, int width, int height, int highscoreIndex) {
        mDifficultyName = difficultyName;
        mNumOfMines = numOfMines;
        mWidth = width;
        mHeight = height;
        mHighscoreIndex = highscoreIndex;
    }

    public int numOfMines() {
        return mNumOfMines;
    }

    public int width() {
        return mWidth;
    }

    public int height() {
        return mHeight;
    }

    public int highscoreIndex() {
        return mHighscoreIndex;
    }

    @Override
    public String toString() {
        return mDifficultyName;
    }
}
