package outercore.parameter;

import beast.core.Input;
import beast.core.parameter.RealParameter;

import java.lang.reflect.Array;
import java.util.TreeMap;

public class KeyRealParameter extends RealParameter {

    public final Input<String> keysInput = new Input<>("keys", "the keys (unique dimension names) for the dimensions of this parameter", (String) null);

    private String[] keys = null;
    private java.util.Map<String, Integer> keyToIndexMap = null;

    @Override
    public void initAndValidate() {
        super.initAndValidate();

        if (keysInput.get() != null) {
            String[] keys = keysInput.get().split(" ");

            // if input should be treated as matrix (2-D array)
            if (getMinorDimension1() > 1 & keys.length != getRowCount()) {
                throw new IllegalArgumentException("Keys must be the same length as minorDimension. minorDimension is " + getMinorDimension2() + ". keys.length = " + keys.length);
            }

            // if input should be treated as 1-D array
            else if (getMinorDimension1() == 1 && keys.length != getDimension()) {
                throw new IllegalArgumentException("Keys must be the same length as dimension. Dimension is " + getDimension() + ". keys.length = " + keys.length);

            }

            initKeys(keys);
        }

        super.initAndValidate();
    }

    private void initKeys(String[] keys) {
        this.keys = keys;

        if (keys != null) {
            keyToIndexMap = new TreeMap<>();

            for (int i = 0; i < keys.length; i++) {
                keyToIndexMap.put(keys[i], i);
            }

            if (keyToIndexMap.keySet().size() != keys.length) {
                throw new IllegalArgumentException("All keys must be unique! Found " + keyToIndexMap.keySet().size() + " unique keys for " + getDimension() + " dimensions.");
            }
        }
    }

    public Double getMax() { return Double.POSITIVE_INFINITY; }

    public Double getMin() { return Double.NEGATIVE_INFINITY;
    }

    public int getColumnCount() {
        return getMinorDimension1();
    }

    public int getRowCount() {
        return getMinorDimension2();
    }

    /**
     * @param i index
     * @return the unique key for the i'th value.
     */
    public String getKey(int i) {
        if (keys != null) return keys[i];

        // return the unique key for the i'th value. Default implementation will return a string representing the zero-based index, (i.e. a string representation of the argument).
        else if (getDimension() == 1) return "0";

        else if (i < getDimension()) return "" + i;

        throw new IllegalArgumentException("Invalid index " + i);
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
