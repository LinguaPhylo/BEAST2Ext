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

    final String spNames = "sp1 sp2 sp3 sp4 sp5 sp6 sp7 sp8 sp9 sp10";
    final String spNames2 = "sp11 sp12 sp13 sp14 sp15 sp16 sp17 sp18 sp19 sp20";

    // each line is a species, each column a trait
    final List<Double> twoTraitsValues =  Arrays.asList(
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

    /*
     * This test checks whether we get all
     * the trait values for two species
     */
    @Test
    public void testDimNamesTwoTraits () {

        KeyRealParameter twoTraits = new KeyRealParameter();
        twoTraits.initByName("value", twoTraitsValues, "dimNames", spNames, "minordimension", 2);

        Assert.assertArrayEquals(twoTraits.getRowValues("sp1"), new Double[] { 0.326278727608277, 1.8164550628074 });
        Assert.assertArrayEquals(twoTraits.getRowValues("sp8"), new Double[] { 4.22298205455098, 1.51483058860744 });
    }

    @Test
    public void testDimNamesOneTrait () {

        KeyRealParameter oneTraits = new KeyRealParameter();
        // pretend to be 1d array now
        oneTraits.initByName("value", twoTraitsValues, "dimNames", spNames+" "+spNames2);

        // 1d array now, so values are diff
        Assert.assertArrayEquals(oneTraits.getRowValues("sp1"), new Double[] { 0.326278727608277 });
        Assert.assertArrayEquals(oneTraits.getRowValues("sp8"), new Double[] { -0.187743059073837 });
        Assert.assertArrayEquals(oneTraits.getRowValues("sp11"), new Double[] { -3.22668212260941 });
        Assert.assertArrayEquals(oneTraits.getRowValues("sp19"), new Double[] { -0.743303344769609 });
    }

//    public void testKeysTwoTraits () {
//
//        KeyRealParameter twoTraits = new KeyRealParameter();
//        // BEAST bug to ignore minordimension for keys : keys.length != dimension
//        twoTraits.initByName("value", twoTraitsValues, "keys", spNames, "minordimension", 2);
//
//        Assert.assertArrayEquals(twoTraits.getRowValues("sp1"), new Double[] { 0.326278727608277, 1.8164550628074 });
//        Assert.assertArrayEquals(twoTraits.getRowValues("sp8"), new Double[] { 4.22298205455098, 1.51483058860744 });
//    }
}