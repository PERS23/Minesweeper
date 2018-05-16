package github.com.PERS23;

import java.io.Serializable;

public class GameStats implements Serializable {

    int[] mBestTimes;

    public GameStats() {
        mBestTimes = new int[3];
        resetStats();
    }

    private int getBestTime(Difficulty difficultyLevel) {
        return mBestTimes[difficultyLevel.highscoreIndex()];
    }

    public void update(int timeTaken, Difficulty difficultyLevel) {
        if (timeTaken < getBestTime(difficultyLevel)) {
            mBestTimes[difficultyLevel.highscoreIndex()] = timeTaken;
        }
    }

    public void resetStats() {
        for (int i = 0; i < 3; i++) {
            mBestTimes[i] = Integer.MAX_VALUE;
        }
    }

    private String getTimeString(Difficulty difficultyLevel) {
        String rep = "";

        if (mBestTimes[difficultyLevel.highscoreIndex()] != Integer.MAX_VALUE) {
            int total = mBestTimes[Difficulty.BEGINNER.highscoreIndex()];
            int seconds = total % 60;
            int minutes = (total / 60) % 60;
            int hours = (total / 60 * 60) % 60;

            rep = String.format("%02dh:%02dm:%02ds", hours, minutes, seconds);
        }

        return rep;
    }

    @Override
    public String toString() {
        return String.format("%-30s%s\n%-30s%s\n%-30s%s",
                "Beginner:",
                getTimeString(Difficulty.BEGINNER),
                "Intermediate:",
                getTimeString(Difficulty.INTERMEDIATE),
                "Expert:",
                getTimeString(Difficulty.EXPERT));
    }
}
