package outercore.parameter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Fabio K. Mendes
 */

/*
 * This class contains unit tests for KeyEnhancedRealParameter
 */
public class KeyRealParameterTest {

    /*
     * This test checks whether we get all
     * the trait values for two species
     */
    @Test
    public void feedTwoTraitsKEParamTest () {

        String spNames = "sp1 sp2 sp3 sp4 sp5 sp6 sp7 sp8 sp9 sp10";

        // each line is a species, each column a trait
        List<Double> twoTraitsValues =  Arrays.asList(
                0.326278727608277, 1.8164550628074,
                -0.370085503473201, 0.665116986641999,
                1.17377224776421, 3.59440970719762,
                3.38137444987329, -0.187743059073837,
                -1.64759474375234, -2.19534387982435,
                -3.22668212260941, -1.71183724870188,
                1.81925405275285, -0.428821390843389,
                4.22298205455098, 1.51483058860744,
                3.63674837097173, 3.68456953445085,
                -0.743303344769609, 1.10602125889508
        );

        KeyRealParameter twoTraits = new KeyRealParameter();
        // BEAST bug to ignore minordimension for keys : keys.length != dimension
        twoTraits.initByName("value", twoTraitsValues, "dimNames", spNames, "minordimension", 2);

        Assert.assertArrayEquals(twoTraits.getRowValues("sp1"), new Double[] { 0.326278727608277, 1.8164550628074 });
        Assert.assertArrayEquals(twoTraits.getRowValues("sp8"), new Double[] { 4.22298205455098, 1.51483058860744 });
    }
}