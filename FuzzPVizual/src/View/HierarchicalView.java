package View;

import java.util.ArrayList;

import javax.swing.SwingConstants;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

import Controller.GlobalController;
import Controller.IGlobalController;
import Model.FuzzyPVizualModel;
//import View.GraphView.SpecificMxGraph;
//import View.GraphView.SpecificMxGraph;
import com.mxgraph.model.mxCell;

public class HierarchicalView implements IView {
    
    private FuzzyPVizualModel model;
    private IGlobalController controller;

    private ArrayList<mxCell> cellList;
    private mxGraph graph;
    private mxGraphComponent graphComponent;

    private static int BIG_SIZE = 40;
    private static int SMALL_SIZE = BIG_SIZE / 10 + 1;
    private static int FONT_SIZE = BIG_SIZE / 2 - SMALL_SIZE;

    private static String TR_NR = "TRNR";
    private static String PL_NR = "PLNR";
    private static String TR_ID_PTTRN = "tr-TRNR";
    private static String PL_ID_PTTRN = "pl-PLNR";

    private static String EDGE_TR_TO_PL_PTTRN = "e-tr-TRNR-pl-PLNR";
    private static String EDGE_PL_TO_TR_PTTRN = "e-pl-PLNR-tr-TRNR";

    private static final String TRANSITION_STYLE = "shape=rectangle;fillColor=#6C939F;strokeColor=#6C939F;fontSize="
            + FONT_SIZE + ";";
    private static final String TRANSITION_STYLE_OUT = "shape=rectangle;fillColor=#6C939F;strokeColor=#104050;strokeWidth=2;fontSize="
            + FONT_SIZE + ";";
    private static final String TRANSITION_STYLE_SELECTED = "shape=rectangle;fillColor=#104050;strokeColor=#104050;fontSize="
            + FONT_SIZE + ";";
    private static final String TRANSITION_STYLE_SELECTED_OUT = "shape=rectangle;fillColor=#104050;strokeColor=#6C939F;strokeWidth=2;fontSize="
            + FONT_SIZE + ";";
    private static final String PLACE_STYLE = "shape=ellipse;fillColor=#6C939F;strokeColor=#104050;fontColor=#104050;fontSize="
            + FONT_SIZE + ";";
    private static final String PLACE_STYLE_INP = "shape=ellipse;fillColor=#6C939F;strokeWidth=3;strokeColor=#104050;fontColor=#104050;fontSize="
            + FONT_SIZE + ";";
    private static final String PLACE_STYLE_SELECTED = "shape=ellipse;fillColor=#104050;strokeWidth=2;strokeColor=#6C939F;fontColor=#6C939F;fontSize="
            + FONT_SIZE + ";";
    private static final String PLACE_STYLE_SELECTED_INP = "shape=ellipse;fillColor=#104050;strokeWidth=4;strokeColor=#6C939F;fontColor=#6C939F;fontSize="
            + FONT_SIZE + ";";
    private static final String EDGE_STYLE = "fillColor=#6C939F;strokeColor=#6C939F;verticalAlign=top;";

    public HierarchicalView(FuzzyPVizualModel m) {
        this.model = m;
        
        graph = new mxGraph();
        this.cellList = new ArrayList<>();
    }

    private void createGraph() {
        Object parent = graph.getDefaultParent();
        System.out.println("start");
        graph.getModel().beginUpdate();
        model.getHierarchicalModel();
        try {

            Object o1 = graph.insertVertex(parent, null,
                    model.getHierarchicalModel().getCellName(), 200, 200, 200, 200);

            Object ooo = graph.insertVertex(parent, null,
                    model.getHierarchicalModel().getCellName(), 200, 200, 200, 200);
            Object oooo = graph.insertEdge(parent, null, "Edge22", o1, ooo);
            runLayoutOrganizer(parent);

            graph.extendParent(o1);

            // need to group to make a structure
            Object o2 = graph.insertVertex(o1, null, model.getHierarchicalModel().getCellName(), 50, 50, 50, 50,
                    PLACE_STYLE);
            Object o3 = graph.insertVertex(o1, null, model.getHierarchicalModel().getCellName(), 50, 50, 50, 50,
                    PLACE_STYLE);
            Object o4 = graph.insertEdge(o1, null, "Edge4", o2, o3);

            // another structure
            Object o5 = graph.insertVertex(o1, null,
                    model.getHierarchicalModel().getCellName(), 30, 30, 30, 30, PLACE_STYLE);
            Object o6 = graph.insertVertex(o1, null,
                    model.getHierarchicalModel().getCellName(), 30, 30, 30, 30, PLACE_STYLE);
            Object o7 = graph.insertVertex(o1, null,
                    model.getHierarchicalModel().getCellName(), 30, 30, 30, 30, TRANSITION_STYLE);
            Object o8 = graph.insertEdge(o1, null,
                    "Edge8", o5, o7);
            Object o9 = graph.insertEdge(o1, null,
                    "Edge9", o6, o7);
            
            runLayoutOrganizer(o1);
        } finally {
            graph.getModel().endUpdate();
        }
        System.out.println("End");
    }

    private void runLayoutOrganizer(Object...objects ) {
      mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
      layout.setOrientation(SwingConstants.WEST);
      layout.setMoveParent(true);
      layout.setResizeParent(true);
      
      for(Object o : objects){
        layout.execute(o);
      }
    }

    public mxGraphComponent createGraphComponent() {

        createGraph();
        graphComponent = new mxGraphComponent(graph);
        return graphComponent;

    }

    
    @Override
    public void reset() {
        createGraph();
        graphComponent.refresh();
    }

    @Override
    public void setController(IGlobalController controller) {
        this.controller = controller;
    }

} 
 
