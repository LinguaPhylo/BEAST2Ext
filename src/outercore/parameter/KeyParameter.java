package outercore.parameter;

import beast.core.parameter.Parameter;

import java.util.TreeMap;

public interface KeyParameter<T extends Number> extends Parameter<T> {


    // set dimNames before this
    default java.util.Map<String, Integer> initDimNames(String[] dimNames, Parameter<T> parameter) {

        if (dimNames != null) {
            // if input should be treated as matrix (2-D array)
            if (parameter.getColumnCount() > 1 & dimNames.length != parameter.getRowCount())
                throw new IllegalArgumentException("Keys must be the same length as minorDimension. " +
                        "minorDimension is " + parameter.getRowCount() + ". dimNames.length = " + dimNames.length);
                // if input should be treated as 1-D array
            else if (parameter.getMinorDimension1() == 1 && dimNames.length != parameter.getDimension()) {
                throw new IllegalArgumentException("Keys must be the same length as dimension. " +
                        "Dimension is " + parameter.getDimension() + ". dimNames.length = " + dimNames.length);
            }

            // init key to index Map
            java.util.Map<String, Integer> keyToIndexMap = new TreeMap<>();

            for (int i = 0; i < dimNames.length; i++)
                keyToIndexMap.put(dimNames[i], i);

            if (keyToIndexMap.keySet().size() != dimNames.length) {
                throw new IllegalArgumentException("All dimNames must be unique! Found " +
                        keyToIndexMap.keySet().size() + " unique dimNames for " + parameter.getDimension() + " dimensions.");
            }

            return keyToIndexMap;
        }

        return null;
    }

}
