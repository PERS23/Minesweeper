package github.com.PERS23;

import java.util.Random;

public class Minefield {

    private Plot[][] mGrid; // [row][col]... [y][x]
    private int mUncoveredPlots;
    private int mSafePlots;
    private int mFlagsLeft;
    private VictoryStatus mVictoryStatus;

    public static Minefield randomField(Difficulty difficultyLevel) {
        return customRandomField(difficultyLevel.width(), difficultyLevel.height(), difficultyLevel.numOfMines());
    }

    /* Creation Algorithm:
     *
     * grid = [n,m]   // initialize all cells to 0
     * for k = 1 to number_of_mines
     *     get random mine_x and mine_y where grid(mine_x, mine_y) is not a mine
     *     for x = -1 to 1
     *         for y = -1 to 1
     *             if x = 0 and y = 0 then
     *                 grid[mine_x, mine_y] = -number_of_mines  // negative value = mine
     *             else
     *                 increment grid[mine_x + x, mine_y + y] by 1
     *
     * Because this algorithm could lead into creating a board with some mines grouped too much together, or worse very
     * dispersed (thus boring to solve), you can then add extra validation when generating mine_x and mine_y number.
     * For example, to ensure that at least 3 neighboring cells are not mines, or even perhaps favor limiting the
     * number of mines that are too far from each other, etc.
     */

    public static Minefield customRandomField(int width, int height, int numOfMines) {
        Minefield randomField = new Minefield(width, height, numOfMines);
        Random randomNumGen = new Random();

        for (int i = 0; i < numOfMines; i++) {
            int mineX = randomNumGen.nextInt(width), mineY = randomNumGen.nextInt(height);
                     // Keep searching for a new square if you're on a mine or if the one you're on has > 5 mines nearby
            while (randomField.isMine(mineX, mineY) || randomField.mGrid[mineY][mineX].getAdjacentMineCount() > 5) {
                mineX = randomNumGen.nextInt(width);
                mineY = randomNumGen.nextInt(height);
            }

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == 0 && y == 0) {
                        randomField.mGrid[mineY][mineX] = new Plot(Integer.MIN_VALUE);
                        randomField.mSafePlots--;
                    } else if (randomField.isWithinBounds(mineX + x, mineY + y)) {
                        randomField.mGrid[mineY + y][mineX + x].incrementAdjacentMineCount();
                    }
                }
            }
        }

        return randomField;
    }

    public Minefield(int width, int height, int numOfMines) {
        mGrid = new Plot[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mGrid[y][x] = new Plot();
            }
        }

        mFlagsLeft = numOfMines;
        mSafePlots = width * height;
        mVictoryStatus = VictoryStatus.PENDING;
    }

    protected boolean isWithinBounds(int x, int y) {
        return (x >= 0 && x < this.getWidth()) && (y >= 0 && y < this.getHeight());
    }

    public int getWidth() {
        return mGrid[0].length;
    }

    public int getHeight() {
        return mGrid.length;
    }

    public int getFlagsLeft() {
        return mFlagsLeft;
    }

    public VictoryStatus getVictoryStatus() {
        return mVictoryStatus;
    }

    protected boolean isMine(int x, int y) {
        return mGrid[y][x].isMine();
    }

    public boolean isFlagged(int x, int y) {
        return mGrid[y][x].isFlagged();
    }

    public boolean isCovered(int x, int y) {
        return mGrid[y][x].isCovered();
    }

    public int getAdjacentMineCount(int x, int y) {
        return mGrid[y][x].getAdjacentMineCount();
    }

    /* Cascade Algorithm:
     *
     * The algorithm resembles a 'flood fill' algorithm and is quite simple if implemented recursively:
     * if a cell contains no neighbouring mines, you can open all neighbouring cells (if not open yet).
     * For each neighbour cell, if it contains no neighbouring mines, you can recursively do the same.
     * If it does contain neighbouring mines, show the number of mines and stop the recursion for that cell
     * (otherwise the algorithm would solve the entire puzzle for you)
     */

    public void uncover(int x, int y) {
        if (isCovered(x, y) && !isFlagged(x, y)) {                      // Only do this if it is covered and not flagged
            if (isMine(x, y)) {                                                            // If this is a mine you lose
                mVictoryStatus = VictoryStatus.FAIL;
            } else {
                mGrid[y][x].uncover();                                                        // Uncover the current one
                mUncoveredPlots++;
                if (mGrid[y][x].getAdjacentMineCount() == 0) {        // Recurse on all neighbours only if non are mines
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if (isWithinBounds(x + i, y + j)) {   // Can't recurse on a tile if it's out of bounds
                                uncover(x + i, y + j);
                            }
                        }
                    }
                } else if (mUncoveredPlots == mSafePlots) {                    // If you've uncovered everything you win
                    mVictoryStatus = VictoryStatus.SUCCESS;
                }
            }
        }
    }

    public void toggleFlag(int x, int y) {
        if (mGrid[y][x].isFlagged()) {
            mFlagsLeft++;
            mGrid[y][x].toggleFlagged();
        } else if (!mGrid[y][x].isFlagged() && mFlagsLeft > 0) {
            mFlagsLeft--;
            mGrid[y][x].toggleFlagged();  // Can't put after, otherwise bug could happen where can flag even tho ran out
        }
    }

    @Override
    public String toString() {
        StringBuilder rep = new StringBuilder();

        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                rep.append(mGrid[y][x]);
            }
            rep.append("\n");
        }

        return rep.toString();
    }
}