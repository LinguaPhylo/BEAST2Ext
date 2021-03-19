package outercore.parameter;

import beast.core.Input;
import beast.core.parameter.RealParameter;

import java.io.PrintStream;
import java.lang.reflect.Array;

public class KeyRealParameter extends RealParameter implements KeyParameter<Double> {

    public final Input<String> keysInput = new Input<>("keys",
            "the keys (unique dimension names) for the dimensions of this parameter", (String) null);

    private String[] keys = null;
    private java.util.Map<String, Integer> keyToIndexMap = null;

    @Override
    public void initAndValidate() {
        super.initAndValidate();

        // set keys before initAndValidateKeys
        if (keysInput.get() != null) {
            this.keys = keysInput.get().split(" ");
        }

        keyToIndexMap = initAndValidateKeys(keys,this);

        // super.initAndValidate(); //duplicate
    }

    @Override
    public Double getMax() { return Double.POSITIVE_INFINITY; }

    @Override
    public Double getMin() { return Double.NEGATIVE_INFINITY; }

    @Override
    public int getColumnCount() {
        return getMinorDimension1();
    }

    @Override
    public int getRowCount() {
        return getMinorDimension2();
    }

    @Override
    public void init(final PrintStream out) {
        init(out, this);
    }

    /**
     * @param i index
     * @return the unique key for the i'th value.
     */
    public String getKey(int i) {
        return getKey(i, this);
    }

    /**
     * @return the array of keys (a unique string for each dimension) that parallels the parameter index.
     */
    public String[] getKeys() {
        /* FKM: @Alexei, why were you calling "new" inside
        a getter -- see the code I commented out (which is
        what you originally wrote, just fixed slightly for
        matricial parameters)?

        Isn't that going to be repeated a gazillion times
        during MCMC?

        Any reason not to just return the 'keys' living
        as states? (what I am now doing)
        */

        /*
        int nKeys = 0;
        if (getMinorDimension1() == 1) {
            nKeys = getDimension();
        }

        else if (getMinorDimension1() > 1) {
            nKeys = getMinorDimension1();
        }

        String[] keys = new String[nKeys];

        for (int i = 0; i < keys.length; i++) {
            keys[i] = getKey(i);
        }
        */
        return this.keys;
    }

    /**
     * @param key unique key for a value
     * @return the value associated with that key, or null
     */
    public Double getValue(String key) {
        if (keys != null) {
            return getValue(keyToIndexMap.get(key));
        }

        try {
            int index = Integer.parseInt(key);
            return getValue(index);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public Double[] getRowValues(String key) {
        String[] keys = getKeys();

        if (keys.length == getRowCount()) {
            for (int row = 0; row < keys.length; row++) {
                if (keys[row].equals(key)) {
                    return getRowValues(row);
                }
            }
        }

        return null;
    }

    public Double[] getRowValues(int row) {

        Double firstVal = getMatrixValue(row,0);

        Double[] values = (Double[])Array.newInstance(firstVal.getClass(), getColumnCount());
        values[0] = firstVal;
        for (int column = 1; column < getColumnCount(); column++) {
            values[column] = getMatrixValue(row, column);
        }

        return values;
    }
}
