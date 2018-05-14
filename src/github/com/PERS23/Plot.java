package github.com.PERS23;

public class Plot {

    private int mAdjacentMineCount; // -ve getAdjacentMineCount means that it is a mine
    private boolean mCovered;
    private boolean mFlagged;

    public Plot() {
        this(0);
    }

    public Plot(int adjacentMineCount) {
        mAdjacentMineCount = adjacentMineCount;
        mCovered = true;
        mFlagged = false;
    }

    public boolean isMine() {
        return mAdjacentMineCount < 0;
    }

    public boolean isCovered() {
        return mCovered;
    }

    public void uncover() {
        mCovered = false;
    }

    public boolean isFlagged() {
        return mFlagged;
    }

    public void toggleFlagged() {
        mFlagged = !mFlagged;
    }

    public int getAdjacentMineCount() {
        return mAdjacentMineCount;
    }

    public void incrementAdjacentMineCount() {
        mAdjacentMineCount++;
    }

    @Override
    public String toString() {
        if (this.isCovered()) {
            return "_ ";
        } else if (this.isFlagged()) {
            return "F ";
        } else if (this.isMine()) {
            return "M ";
        } else {
            return getAdjacentMineCount() + " ";
        }
    }
}
