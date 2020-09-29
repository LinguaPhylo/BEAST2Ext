package outercore.parameter;

import beast.core.Description;
import beast.core.Input;
import beast.core.parameter.Parameter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.TreeMap;

public interface KeyParameter<T> extends Parameter<T> {

    T getValue(int i);

    default int getColumnCount() {
        return getMinorDimension1();
    }

    default int getRowCount() {
        return getMinorDimension2();
    }

    /**
     * @param key unique key for a value
     * @return the value associated with that key, or null
     */
    default T getValue(String key) {
        try {
            int index = Integer.parseInt(key);
            return getValue(index);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    /**
     * Interprets the parameter as a two dimensional matrix, with minorDimension1 column width.
     *
     * @param row zero-based row index
     * @param column zero-based column index
     * @return
     */
    default T getMatrixValue(int row, int column) {
        return getValue(row * getColumnCount() + column);
    }

    default T[] getRowValues(String key) {
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

    default T[] getRowValues(int row) {

        T firstVal = getMatrixValue(row,0);

        T[] values = (T[]) Array.newInstance(firstVal.getClass(), getColumnCount());
        values[0] = firstVal;
        for (int column = 1; column < getColumnCount(); column++) {
            values[column] = getMatrixValue(row, column);
        }
        return values;
    }

    /**
     * @param i index
     * @return the unique key for the i'th value. Default implementation will return a string representing the zero-based index, (i.e. a string representation of the argument).
     */
    default String getKey(int i) {
        if (getDimension() == 1) return "0";
        else if (i < getDimension()) return "" + i;
        throw new IllegalArgumentException("Invalid index " + i);
    }

    /**
     * @return the array of keys (a unique string for each dimension) that parallels the parameter index.
     */
    default String[] getKeys() {
        String[] keys = new String[getDimension()];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = getKey(i);
        }
        return keys;
    }

    @Description("A parameter represents a value in the state space that can be changed by operators.")
    public abstract class KeyBase<T> extends Base<T> implements KeyParameter<T> {

        public final Input<String> keysInput = new Input<>("keys", "the keys (unique dimension names) for the dimensions of this parameter", (String) null);

        private String[] keys = null;
        private java.util.Map<String, Integer> keyToIndexMap = null;

        /**
         * constructors *
         */
        public KeyBase() {
        }

        public KeyBase(final T[] values) {
            super(values);
        }

        @Override
        public void initAndValidate() {
            T[] valuesString = valuesInput.get().toArray((T[]) Array.newInstance(getMax().getClass(), 0));

            int dimension = Math.max(dimensionInput.get(), valuesString.length);
            dimensionInput.setValue(dimension, this);
            values = (T[]) Array.newInstance(getMax().getClass(), dimension);
            storedValues = (T[]) Array.newInstance(getMax().getClass(), dimension);
            for (int i = 0; i < values.length; i++) {
                values[i] = valuesString[i % valuesString.length];
            }

            m_bIsDirty = new boolean[dimensionInput.get()];

            minorDimension = minorDimensionInput.get();
            if (minorDimension > 0 && dimensionInput.get() % minorDimension > 0) {
                throw new IllegalArgumentException("Dimension must be divisible by stride");
            }
            this.storedValues = values.clone();

            if (keysInput.get() != null) {
                String[] keys = keysInput.get().split(" ");
                if (keys.length != dimension) {
                    throw new IllegalArgumentException("Keys must be the same length as dimension. Dimension is " + dimension + ". keys.length = " + keys.length);
                }
                initKeys(keys);
            }
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

        abstract T getMax();

        abstract T getMin();

        /**
         * @param i index
         * @return the unique key for the i'th value.
         */
        public String getKey(int i) {
            if (keys != null) return keys[i];
            return KeyParameter.super.getKey(i);
        }

        /**
         * @param key unique key for a value
         * @return the value associated with that key, or null
         */
        public T getValue(String key) {
            if (keys != null) {
                return getValue(keyToIndexMap.get(key));
            }
            return KeyParameter.super.getValue(key);
        }

        /**
         * @return a sorted set of valid keys for this parameter.
         */
        public String[] getKeys() {
            if (keys != null) return Arrays.copyOf(keys, keys.length);
            return KeyParameter.super.getKeys();
        }

        /**
         * matrix implementation *
         */
        @Override
        public int getMinorDimension1() {
            return minorDimension;
        }

        @Override
        public int getMinorDimension2() {
            return getDimension() / minorDimension;
        }

        @Override
        public T getMatrixValue(final int i, final int j) {
            return values[i * minorDimension + j];
        }

        public void setMatrixValue(final int i, final int j, final T value) {
            setValue(i * minorDimension + j, value);
        }

        public void getMatrixValues1(final int i, final T[] row) {
            assert (row.length == minorDimension);
            System.arraycopy(values, i * minorDimension, row, 0, minorDimension);
        }

        public void getMatrixValues1(final int i, final double[] row) {
            assert (row.length == minorDimension);
            for (int j = 0; j < minorDimension; j++) {
                row[j] = getArrayValue(i * minorDimension + j);
            }
        }

        public void getMatrixValues2(final int j, final T[] col) {
            assert (col.length == getMinorDimension2());
            for (int i = 0; i < getMinorDimension2(); i++) {
                col[i] = values[i * minorDimension + j];
            }
        }

        public void getMatrixValues2(final int j, final double[] col) {
            assert (col.length == getMinorDimension2());
            for (int i = 0; i < getMinorDimension2(); i++) {
                col[i] = getArrayValue(i * minorDimension + j);
            }
        }

        /**
         * Restore a saved parameter from string representation. This cannot be
         * a template method since it requires creation of an array of T...
         *
         * @param dimension parameter dimension
         * @param lower lower bound
         * @param upper upper bound
         * @param values values
         */
        abstract void fromXML(int dimension, String lower, String upper, String[] values);
    }
}
