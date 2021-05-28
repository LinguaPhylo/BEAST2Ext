package outercore.parameter;

import beast.core.Input;
import beast.core.parameter.IntegerParameter;

import java.io.PrintStream;
import java.util.Arrays;

@Deprecated
public class KeyIntegerParameter extends IntegerParameter implements KeyParameter<Integer> {

//    public final Input<String> keysInput = new Input<>("keys",
//            "the keys (unique dimension names) for the dimensions of this parameter", (String) null);
    public final Input<Boolean> idStart1Input = new Input<>("idStart1",
            "If true, the element's name will start from 1 instead of 0, " +
                    "which is same as BEAST Parameter convention.", false);


    public static KeyIntegerParameter createKeyIntegerParameter(IntegerParameter param) {
        KeyIntegerParameter newParam = new KeyIntegerParameter();
        newParam.setInputValue("value", Arrays.asList(param.getValues()));
        newParam.setInputValue("dimension", param.getDimension());
        newParam.setInputValue("lower", param.getLower());
        newParam.setInputValue("upper", param.getUpper());
        newParam.setInputValue("estimate", newParam.isEstimated());

        newParam.setInputValue("keys", String.join(" ", param.getKeys()));

        newParam.setID(param.getID());
        newParam.initAndValidate();
        return newParam;
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




//    private String[] keys = null;
//    private java.util.Map<String, Integer> keyToIndexMap = null;
//
//    @Override
//    public void initAndValidate() {
//        super.initAndValidate();
//
//        // set keys before initAndValidateKeys
//        if (keysInput.get() != null) {
//            this.keys = keysInput.get().split(" ");
//        }
//
//        keyToIndexMap = initAndValidateKeys(keys, this);
//    }
//
//    @Override
//    public Integer getMax() {
//        return Integer.MAX_VALUE - 1;
//    }
//
//    @Override
//    public Integer getMin() {
//        return Integer.MIN_VALUE + 1;
//    }
//
//    @Override
//    public int getColumnCount() {
//        return getMinorDimension1();
//    }
//
//    @Override
//    public int getRowCount() {
//        return getMinorDimension2();
//    }

//    /**
//     * @return the array of keys (a unique string for each dimension) that parallels the parameter index.
//     */
//    public String[] getKeys() {
//        return this.keys;
//    }
//
//
//    public Integer getValue(String key) {
//        if (keys != null) {
//            return getValue(keyToIndexMap.get(key));
//        }
//
//        try {
//            int index = Integer.parseInt(key);
//            return getValue(index);
//        } catch (NumberFormatException nfe) {
//            return null;
//        }
//    }

}
