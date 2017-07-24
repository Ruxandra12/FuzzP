package structure;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

public interface IHierarchicalModel {

    int getNrOfComponents();

    Object getParent(Object parent);

    String getCellName(int id);

    String getEdgeName(int id);

    int getObjectId();

}
