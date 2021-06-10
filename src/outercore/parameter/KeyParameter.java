package outercore.parameter;

import beast.core.parameter.Parameter;

import java.util.List;
import java.util.TreeMap;

public interface KeyParameter<T extends Number> extends Parameter<T> {


    // set dimNames before this
    default java.util.Map<String, Integer> initDimNames(List<String> dimNames, Parameter<T> parameter) {

        if (dimNames != null) {
            // getRowCount() = getDimension() / minorDimension, getColumnCount() = minorDimension
            if (parameter.getColumnCount() > 1) { // 2-D matrix
                if (! (dimNames.size() == parameter.getRowCount() || dimNames.size() == parameter.getDimension()) )
                    throw new IllegalArgumentException("For 2D matrix, dimNames must either have the same length " +
                            "as dimension or the number of rows ! Number of rows = " +
                            parameter.getRowCount() + ", but dimNames.length = " + dimNames.size());
            } else { // 1-D array
                if (dimNames.size() != parameter.getDimension())
                    throw new IllegalArgumentException("For 1D array, dimNames must have the same length as dimension ! " +
                            "Dimension = " + parameter.getDimension() + ", but dimNames.length = " + dimNames.size());
            }

            // init key to index Map
            java.util.Map<String, Integer> keyToIndexMap = new TreeMap<>();

            for (int i = 0; i < dimNames.size(); i++)
                keyToIndexMap.put(dimNames.get(i), i);

            if (keyToIndexMap.keySet().size() != dimNames.size()) {
                throw new IllegalArgumentException("All dimNames must be unique! Found " +
                        keyToIndexMap.keySet().size() + " unique dimNames for " + parameter.getDimension() + " dimensions.");
            }

            return keyToIndexMap;
        }

        return null;
    }

}
