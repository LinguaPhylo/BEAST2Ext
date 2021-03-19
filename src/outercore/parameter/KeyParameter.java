package outercore.parameter;

import beast.core.parameter.Parameter;

import java.io.PrintStream;
import java.util.TreeMap;

public interface KeyParameter<T extends Number> {

    /**
     * @return the array of keys (a unique string for each dimension) that parallels the parameter index.
     */
    String[] getKeys();

    /**
     * @return number of columns, if input should be treated as matrix (2-D array)
     */
    int getColumnCount();

    /**
     * @return number of rows, if input should be treated as matrix (2-D array)
     */
    int getRowCount();


    T getMax();

    T getMin();

    T getValue(String key);

    // set keys before this
    default java.util.Map<String, Integer> initAndValidateKeys(String[] keys, Parameter<T> parameter) {

        if (keys != null) {
            // if input should be treated as matrix (2-D array)
            if (getColumnCount() > 1 & keys.length != getRowCount())
                throw new IllegalArgumentException("Keys must be the same length as minorDimension. " +
                        "minorDimension is " + getRowCount() + ". keys.length = " + keys.length);
            // if input should be treated as 1-D array
            else if (parameter.getMinorDimension1() == 1 && keys.length != parameter.getDimension()) {
                throw new IllegalArgumentException("Keys must be the same length as dimension. " +
                        "Dimension is " + parameter.getDimension() + ". keys.length = " + keys.length);
            }

            // init key to index Map
            java.util.Map<String, Integer> keyToIndexMap = new TreeMap<>();

            for (int i = 0; i < keys.length; i++)
                keyToIndexMap.put(keys[i], i);

            if (keyToIndexMap.keySet().size() != keys.length) {
                throw new IllegalArgumentException("All keys must be unique! Found " +
                        keyToIndexMap.keySet().size() + " unique keys for " + parameter.getDimension() + " dimensions.");
            }

            return keyToIndexMap;
        }

        return null;
    }


    default void initOut(final PrintStream out, Parameter<T> parameter, boolean idStart1) {
        final int valueCount = parameter.getDimension();
        if (valueCount == 1) {
            out.print(parameter.getID() + "\t");
        } else {
            for (int i = 0; i < valueCount; i++) {
                out.print(parameter.getID() + "." + getKey(i, parameter, idStart1) + "\t");
            }
        }
    }

    /**
     * @param i index
     * @return the unique key for the i'th value.
     */
    default String getKey(int i, Parameter<T> parameter, boolean idStart1) {
        String[] keys = getKeys();
        if (keys != null) return keys[i];

        // return the unique key for the i'th value.
        // Default implementation will return a string representing the zero-based index,
        // (i.e. a string representation of the argument).
        else if (parameter.getDimension() == 1) return "0";

        // i start from 0
        else if (i < parameter.getDimension()) {
            if (idStart1) return "" + (i+1);
            return "" + i;
        }

        throw new IllegalArgumentException("Invalid index " + i);
    }


}
