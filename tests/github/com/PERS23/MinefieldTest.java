package github.com.PERS23;

import org.junit.Test;

import static org.junit.Assert.*;

public class MinefieldTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @Test
    public void testToString() {
        Minefield instance = Minefield.randomField(Difficulty.EXPERT);
        instance.uncover(6, 3);
        System.out.println(instance.toString());
    }
}