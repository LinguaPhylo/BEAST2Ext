package outercore.parameter;

import beast.core.Input;
import beast.core.parameter.RealParameter;

import java.util.Arrays;

public class KeyRealParameter extends RealParameter implements KeyParameter<Double> {

    public final Input<String> dimNamesInput = new Input<>("dimNames",
            "the keys (unique dimension names) for the dimensions of this parameter",
            null, Input.Validate.XOR, keysInput);

    private String[] dimNames = null;
    private java.util.Map<String, Integer> dimNameToIndexMap = null;


    @Override
    public void initAndValidate() {

        super.initAndValidate();

        // set keys before initAndValidateKeys
        if (dimNamesInput.get() != null) {
            this.dimNames = dimNamesInput.get().split(" ");
        }

        dimNameToIndexMap = initDimNames(dimNames,this);
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
    public Double getValue(String key) {
        if (dimNames != null) {
            return getValue(dimNameToIndexMap.get(key));
        }
        return super.getValue(key);
    }

    public Double[] getRowValues(String key) {
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
