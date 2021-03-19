package outercore.parameter;

import beast.core.Input;
import beast.core.parameter.IntegerParameter;

import java.io.PrintStream;

public class KeyIntegerParameter extends IntegerParameter implements KeyParameter<Integer> {

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

        keyToIndexMap = initAndValidateKeys(keys, this);
    }

    @Override
    public Integer getMax() {
        return Integer.MAX_VALUE - 1;
    }

    @Override
    public Integer getMin() {
        return Integer.MIN_VALUE + 1;
    }

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
        return this.keys;
    }


    public Integer getValue(String key) {
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

}
