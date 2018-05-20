package github.com.PERS23.Minesweeper;

import org.junit.Test;

import static org.junit.Assert.*;

public class MinefieldTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @Test
    public void itShouldCorrectlyLabelTheAdjacencyNumberOfAllPlots() {
        Minefield instance = Minefield.randomField(Difficulty.EXPERT);

        for (int y = 0; y < instance.getHeight(); y++) {
            for (int x = 0; x < instance.getWidth(); x++) {
                if (!instance.isMine(x, y)) {
                    int count = 0;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if (instance.isWithinBounds(x + i, y + j) && instance.isMine(x + i, y + j)) {
                                count++;
                            }
                        }
                    }
                    assertEquals(count, instance.getAdjacentMineCount(x, y));
                }
            }
        }
    }

}