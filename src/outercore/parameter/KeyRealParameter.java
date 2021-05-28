package outercore.parameter;

import beast.core.Input;
import beast.core.parameter.RealParameter;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class KeyRealParameter extends RealParameter implements KeyParameter<Double> {

    public final Input<String> demeNamesInput = new Input<>("dimNames",
            "the keys (unique dimension names) for the dimensions of this parameter",
            (String) null, Input.Validate.XOR, keysInput);

    public final Input<Boolean> idStart1Input = new Input<>("idStart1",
            "If true, the element's name will start from 1 instead of 0, " +
                    "which is same as BEAST Parameter convention.", false);


    private String[] keys = null;
    private java.util.Map<String, Integer> keyToIndexMap = null;


    public static KeyRealParameter createKeyRealParameter(RealParameter param) {
        List<Double> values = Arrays.asList(param.getValues());
        String[] keys = param.getKeys();

        if (values.size() != keys.length)
            throw new UnsupportedOperationException("Please use Input dimNames instead ! " +
                    "Otherwise values and keys must have the same length.");

        KeyRealParameter newParam = new KeyRealParameter();
        newParam.setInputValue("value", values);
        newParam.setInputValue("dimension", param.getDimension());
        newParam.setInputValue("lower", param.getLower());
        newParam.setInputValue("upper", param.getUpper());
        newParam.setInputValue("estimate", newParam.isEstimated());

        newParam.setInputValue("dimNames", String.join(" ", keys));

        newParam.setID(param.getID());
        newParam.initAndValidate();
        return newParam;
    }


    @Override
    public void initAndValidate() {
        super.initAndValidate();

        // set keys before initAndValidateKeys
        if (demeNamesInput.get() != null) {
            this.keys = demeNamesInput.get().split(" ");
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
        initOut(out, this, idStart1Input.get());
    }

    /**
     * @param i index
     * @return the unique key for the i'th value.
     */
    public String getKey(int i) {
        return getKey(i, this, idStart1Input.get());
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
