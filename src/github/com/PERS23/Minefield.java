package github.com.PERS23;

import java.util.Random;

public class Minefield {

    private Plot[][] mGrid; // [row][col]... [y][x]
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
        mVictoryStatus = VictoryStatus.PENDING;
    }

    private boolean isWithinBounds(int x, int y) {
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

    public boolean isMine(int x, int y) {
        return mGrid[y][x].isMine();
    }

    public boolean isFlagged(int x, int y) {
        return mGrid[y][x].isFlagged();
    }

    public boolean isCovered(int x, int y) {
        return mGrid[y][x].isCovered();
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
        if (isCovered(x, y) && !isFlagged(x, y)) {                       // Only do this if it is covered and not marked
            if (isMine(x, y)) {                                                            // If this is a mine you lose
                mVictoryStatus = VictoryStatus.FAIL;
            } else {
                mGrid[y][x].uncover();                                                        // Uncover the current one

                if (mGrid[y][x].getAdjacentMineCount() == 0) {        // Recurse on all neighbours only if non are mines
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if (isWithinBounds(x + i, y + j)) {   // Can't recurse on a tile if it's out of bounds
                                uncover(x + i, y + j);
                            }
                        }
                    }
                } else {
                    // Check if over
                }
            }
        }
    }

    public void flag(int x, int y) {

    }

    @Override
    public String toString() {
        StringBuilder rep = new StringBuilder();

        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                if (isMine(x, y)) {
                    rep.append("M ");
                } else if (isCovered(x, y)) {
                    rep.append("_ ");
                } else {
                    rep.append(mGrid[y][x].getAdjacentMineCount() + " ");
                }
            }
            rep.append("\n");
        }

        return rep.toString();
    }
}

/* You win by clearing all the safe squares and lose if you click on a mine.
 * A mine counter tells you how many mines are still hidden and a time counter keeps track of your score.
 */