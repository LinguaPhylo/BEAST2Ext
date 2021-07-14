package outercore.util;

import beast.core.BEASTInterface;
import beast.core.BEASTObject;
import beast.core.Input;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class BEASTVector extends BEASTObject {

    public Input<List<BEASTInterface>> vectorInput = new Input<>("element", "a vector of beast objects.", new ArrayList<>());

    public BEASTVector(List<BEASTInterface> elements, String id) {
        setInputValue("element", elements);
        initAndValidate();
        setID(id);
    }

    public BEASTVector(List<BEASTInterface> elements) {
        setInputValue("element", elements);
        initAndValidate();
    }

    @Override
    public void initAndValidate() {}

    public List<BEASTInterface> getObjectList() {
        return vectorInput.get();
    }
}
