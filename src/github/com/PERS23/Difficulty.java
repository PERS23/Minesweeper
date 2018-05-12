package github.com.PERS23;

public enum Difficulty {
    BEGINNER("Beginner", 10, 9, 9),
    INTERMEDIATE("Intermediate", 40, 16, 16),
    EXPERT("Expert", 99, 30, 16);

    private String mDifficultyName;
    private int mNumOfMines;
    private int mWidth;
    private int mHeight;

    private Difficulty(String difficultyName, int numOfMines, int width, int height) {
        mDifficultyName = difficultyName;
        mNumOfMines = numOfMines;
        mWidth = width;
        mHeight = height;
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

    @Override
    public String toString() {
        return mDifficultyName;
    }
}
