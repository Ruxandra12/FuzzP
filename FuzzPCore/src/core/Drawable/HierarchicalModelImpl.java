package core.Drawable;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import structure.IHierarchicalModel;

public class HierarchicalModelImpl implements IHierarchicalModel {

    public HierarchicalModelImpl() {

    }

    @Override
    public int getNrOfComponents() {

        return 2;
    }

    @Override
    public String getCellName(int Id) {
        return "Cell " + Id;

    }

    @Override
    public String getEdgeName(int Id) {
        return "Edge " + Id;

    }

    @Override
    public Object getParent(Object parent) {
        return parent;

    }

    @Override
    public int getObjectId() {
        int id = 0;
        for (int i = 0; i < 10; i++)
            id = i;
        return id;
    }

}
