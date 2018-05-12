package github.com.PERS23;

import java.util.Random;

public class Minefield {

    private Plot[][] mGrid; // [row][col]... [y][x]
    private int mFlagsLeft;

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

            while (randomField.isMine(mineX, mineY) && randomField.hasLessThan3MineNeighbours(mineX, mineY)) {
                mineX = randomNumGen.nextInt(width);
                mineY = randomNumGen.nextInt(height);
            }

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == 0 && y == 0) {
                        randomField.makePlotAMine(mineX, mineY);
                    } else {
                        randomField.incrementPlotMineAdjacencyNum(mineX + x, mineY + y);
                    }
                }
            }
        }

        return randomField;
    }

    public Minefield(int width, int height, int numOfMines) {
        mGrid = new Plot[height][width];
        mFlagsLeft = numOfMines;
    }

    private boolean hasLessThan3MineNeighbours(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {

            }
        }

        return true;
    }

    private void incrementPlotMineAdjacencyNum(int x, int y) {
        if ((x >= 0 && x < this.getWidth()) && (y >= 0 && y < this.getHeight())) {
            mGrid[y][x] = mGrid[y][x].incrementNumOfAdjacent();
        }
    }

    private void makePlotAMine(int x, int y) {
        if ((x >= 0 && x < this.getWidth()) && (y >= 0 && y < this.getHeight())) {
            mGrid[y][x] = new Plot(Integer.MIN_VALUE);
        }
    }

    public int getFlagsLeft() {
        return mFlagsLeft;
    }

    public boolean isMine(int x, int y) {
        return mGrid[y][x].isMine();
    }

    public int getWidth() {
        return mGrid[0].length;
    }

    public int getHeight() {
        return mGrid.length;
    }
}

/* You win by clearing all the safe squares and lose if you click on a mine.
 * A mine counter tells you how many mines are still hidden and a time counter keeps track of your score.
 */

/* Uncover Algorithm:
 *
 * The algorithm resembles a 'flood fill' algorithm and is quite simple if implemented recursively:
 * if a cell contains no neighbouring mines, you can open all neighbouring cells (if not open yet).
 * For each neighbour cell, if it contains no neighbouring mines, you can recursively do the same.
 * If it does contain neighbouring mines, show the number of mines and stop the recursion for that cell
 * (otherwise the algorithm would solve the entire puzzle for you)
 */