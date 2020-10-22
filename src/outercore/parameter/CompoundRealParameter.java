package outercore.parameter;

import beast.core.parameter.RealParameter;

/**
 * Fixes some bugs in the implementation of the BEASTLabs CompoundRealParameter
 */
public class CompoundRealParameter extends beast.core.parameter.CompoundRealParameter {

    public Double getLower() {
        return parameterListInput.get().get(0).getLower();
    }

    public Double getUpper() {
        return parameterListInput.get().get(0).getUpper();
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(getID()).append("[").append(parameterListInput.get().size());
        if (minorDimension > 0) {
            buf.append(" ").append(minorDimension);
        }
        buf.append("] ");
        buf.append("(").append(getLower()).append(",").append(getUpper()).append("): ");
        for (final RealParameter value : parameterListInput.get()) {
            buf.append(value).append(" ");
        }
        return buf.toString();
    }

    @Override
    public CompoundRealParameter copy() {
        try {
            @SuppressWarnings("unchecked") final CompoundRealParameter copy = (CompoundRealParameter) this.clone();
            return copy;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
