package github.com.PERS23;

public class Plot {

    private int mNumOfAdjacent; // -ve numOfAdjacent means that it is a mine
    private boolean mCovered;
    private boolean mFlagged;

    public Plot() {
        this(0);
    }

    public Plot(int numOfAdjacent) {
        mNumOfAdjacent = numOfAdjacent;
        mCovered = true;
        mFlagged = false;
    }

    public boolean isMine() {
        return mNumOfAdjacent < 0;
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

    public int numOfAdjacent() {
        return mNumOfAdjacent;
    }

    public Plot incrementNumOfAdjacent() {
        return new Plot(mNumOfAdjacent++);
    }
}
