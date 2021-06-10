package outercore.parameter;

import beast.core.Input;
import beast.core.parameter.IntegerParameter;

import java.util.Arrays;

public class KeyIntegerParameter extends IntegerParameter implements KeyParameter<Integer> {

    public final Input<String> dimNamesInput = new Input<>("dimNames",
            "the unique dimension names for the dimensions of this parameter, " +
                    "which can also be either same or the divisor of the values length. ",
            null, Input.Validate.XOR, keysInput);

    private String[] dimNames = null;
    private java.util.Map<String, Integer> dimNameToIndexMap = null;


    @Override
    public void initAndValidate() {
        // set keys before initAndValidateKeys
        if (dimNamesInput.get() != null) {
            this.dimNames = dimNamesInput.get().split(" ");
        }

        dimNameToIndexMap = initDimNames(dimNames,this);

        super.initAndValidate();
    }

    /**
     * @param i index
     * @return the unique key for the i'th value.
     */
    public String getKey(int i) {
        if (dimNames != null) return dimNames[i];
        return super.getKey(i);
    }

    /**
     * @return the array of keys (a unique string for each dimension) that parallels the parameter index.
     */
    public String[] getKeys() {
        if (dimNames != null) return Arrays.copyOf(dimNames, dimNames.length);
        return super.getKeys();
    }

    /**
     * @param key unique key for a value
     * @return the value associated with that key, or null
     */
    public Integer getValue(String key) {
        if (dimNames != null) {
            return getValue(dimNameToIndexMap.get(key));
        }
        return super.getValue(key);
    }

    public Integer[] getRowValues(String key) {
        String[] keys = this.getKeys();

        if (keys.length == getRowCount()) {
            for (int row = 0; row < keys.length; row++) {
                if (keys[row].equals(key)) {
                    return getRowValues(row);
                }
            }
        }

        return null;
    }

}