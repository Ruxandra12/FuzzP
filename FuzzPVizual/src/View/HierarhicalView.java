package View;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.SwingConstants;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

import Controller.IGlobalController;
import Model.FuzzyPVizualModel;
import structure.IHierarchicalComponent;

public class HierarhicalView implements IView {

    private static int PARENT_SIZE = 100;
    private static int CHILDREN_SIZE = 50;
    private static int INOUT_SIZE = 15;
    private static int FONT_SIZE = 20;

    private static String TR_NR = "TRNR";
    private static String PL_NR = "PLNR";
    private static String TR_ID_PTTRN = "tr-TRNR";
    private static String PL_ID_PTTRN = "pl-PLNR";

    private static final String PLACE_STYLE_SELECTED = "shape=ellipse;fillColor=#104050;strokeWidth=2;strokeColor=#6C939F;fontColor=#6C939F;fontSize="
            + FONT_SIZE + ";";
    private static final String IN_OUT_COMP = "shape=ellipse;fillColor=#6C939F;strokeWidth=3;strokeColor=#104050;fontColor=#104050;fontSize="
            + INOUT_SIZE + ";";

    private static final String CHILDREN_STYLE = "shape=rectangle;fillColor=#006bff;strokeColor=#104050;fontColor=#104050;fontSize="
            + CHILDREN_SIZE + ";";

    private static final String PARENT_STYLE = "shape=rectangle;fillColor=#00fff9;strokeColor=#104050;fontColor=#104050;fontSize="
            + FONT_SIZE + ";";

    private static final String EDGE_STYLE = "fillColor=#6C939F;strokeColor=#6C939F;verticalAlign=top;";

    private IGlobalController controller;
    private mxGraph graph;
    private mxGraphComponent graphComponent;
    private int counter;

    private HashMap<IHierarchicalComponent, mxCell> componentMap;
    private HashMap<IHierarchicalComponent.Edge, mxCell> edgeMap;
    private HashMap<String, mxCell> InpcompMap;
    private HashMap<String, mxCell> OutcompMap;
    FuzzyPVizualModel model;

    public HierarhicalView(FuzzyPVizualModel model) {
        this.model = model;
        graph = new mxGraph();
        componentMap = new HashMap<>();
        edgeMap = new HashMap<>();
        InpcompMap = new HashMap<>();
        OutcompMap = new HashMap<>();
    }

    public void createGraph() {
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        model.getHierarchicalModel();
        System.out.println("Creeaza");
        try {

            List<IHierarchicalComponent> mainComp = Arrays.asList(model.getHierarchicalModel());

            drawCell(parent, mainComp, new ArrayList<>(),0,0);

            graph.setCellsSelectable(false);
            graph.setCellsEditable(false);

        } finally {
            graph.getModel().endUpdate();
            System.out.println("Finish");
        }
    }

    public mxGraphComponent createGraphComponent() {
        createGraph();
        graphComponent = new mxGraphComponent(graph);
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                if (cell != null) {
                    elementIsClicked(cell.getId(), e.getButton() == 1);
                }
            }

        });

        return graphComponent;

    }

    public void drawCell(Object parent, List<IHierarchicalComponent> components,
            List<IHierarchicalComponent.Edge> edges, int counterIn, int counterOut) {

        for (IHierarchicalComponent comp : components) {

            mxCell componentCell = (mxCell) graph.insertVertex(parent, null, comp.getCellName(), 0, 0, PARENT_SIZE,
                    PARENT_SIZE, PARENT_STYLE);

            componentMap.put(comp, componentCell);

            for (int i = 0; i < comp.getInputs().size(); i++) {

                mxCell cell = (mxCell) graph.insertVertex(parent, "i" + Integer.toString(counterIn), comp.getInputs().get(i), 0,
                        0, CHILDREN_SIZE, CHILDREN_SIZE, IN_OUT_COMP);
                mxCell edg = (mxCell) graph.insertEdge(parent, null, "", cell, componentCell);
                InpcompMap.put("i" + Integer.toString(counterIn), cell);
                counterIn++;

//                System.out.println("Size of input list ! " + comp.getInputs().size());
             //  System.out.println("Inputs: "+comp.getInputs().get(i));
               System.out.println("InputCell id: " + cell.getId()+" Cell value: "+cell.getValue());
            }

            for (int i = 0; i < comp.getOutputs().size(); i++) {

                mxCell cell = (mxCell) graph.insertVertex(parent, "o" + Integer.toString(counterOut), comp.getOutputs().get(i),
                        0, 0, CHILDREN_SIZE, CHILDREN_SIZE, IN_OUT_COMP);
                mxCell edg = (mxCell) graph.insertEdge(parent, null, "", componentCell, cell);
                OutcompMap.put("o" + Integer.toString(counterOut), cell);
                counterOut++;

//                 System.out.println("Size of output list ! "+comp.getOutputs().size());
//                 System.out.println("Outputs: "+comp.getOutputs().get(i));
                 System.out.println("OutputCell id: "+cell.getId());
            }

        }

        for (IHierarchicalComponent.Edge edge : edges) {
            mxCell edg = (mxCell) graph.insertEdge(parent, null, "", componentMap.get(edge.source),
                    componentMap.get(edge.dest));
            edgeMap.put(edge, edg);
        }

        runLayoutOrganizer(parent);

        for (IHierarchicalComponent comp : components) {
            drawCell(componentMap.get(comp), comp.getChildrenComponents(), comp.getChildEdges(), counterIn, counterOut);

        }
//        System.out.println(">>>>>>>>>>>>>" + InpcompMap);
//        System.out.println(">>>>>>>>>>>>>" + OutcompMap);
  
        runLayoutOrganizer(parent);
    }

    public void runLayoutOrganizer(Object... objects) {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(SwingConstants.NORTH);
        layout.setOrientation(SwingConstants.WEST);
        layout.setMoveParent(true);
        layout.setResizeParent(true);
        layout.isResizeParent();

        for (Object o : objects) {
            layout.execute(o);
        }
    }

    @Override
    public void setController(IGlobalController controller) {
        this.controller = controller;
    }

    @Override
    public void reset() {
        graph.removeCells();
        graph.removeCells(componentMap.values().toArray());
        graph.removeCells(InpcompMap.values().toArray());
        graph.removeCells(OutcompMap.values().toArray());
        componentMap = new HashMap<>();
        edgeMap = new HashMap<>();
        InpcompMap = new HashMap<>();
        OutcompMap = new HashMap<>();

        createGraph();
        graphComponent.refresh();
        graph.refresh();

    }

    private void elementIsClicked(String id, boolean selectioOrDeselect) {
        if (cellIsPlace(id)) {
            publishPlaceSelection(id, selectioOrDeselect);
        }
        if (cellIsTransition(id)) {
            publishTransitionSelection(id, selectioOrDeselect);
        }
    }

    boolean cellIsPlace(String id) {
        return id.startsWith("i");
    }

    boolean cellIsTransition(String id) {
        return id.startsWith("o");
    }

    private void publishTransitionSelection(String id, boolean selectioOrDeselect) {
        int trId = extratTransitionId(id);
        if (selectioOrDeselect) {
            controller.tranSelectionRequest(trId);
        } else {
            controller.tranDeselectionRequest(trId);
        }
    }

    private void publishPlaceSelection(String id, boolean selectioOrDeselect) { // notify a selection
        int plnr = extractPlaceId(id);
        if (selectioOrDeselect) {
            controller.placeSelectionReqiest(plnr);
        } else {
            controller.placeDeselectionReqiest(plnr);
        }
    }

    @Override
    public void transitionSelected(int trId) {
        System.out.println("OutCompId: " + trId);
        OutcompMap.get("o" + Integer.toString(trId)).setStyle(PLACE_STYLE_SELECTED);
        graphComponent.refresh();
    }

    @Override
    public void transitionDeselected(int trId) {
        OutcompMap.get("o" + Integer.toString(trId)).setStyle(IN_OUT_COMP);
        graphComponent.refresh();
    }

    @Override
    public void placeSelected(int plId) {
        System.out.println("Input ID: " + plId);
        System.out.println("-------" + InpcompMap.get("i" + Integer.toString(plId)).getId());
        System.out.println("------->" + InpcompMap.get("i" + Integer.toString(plId)).getId().toString());
        System.out.println("------->>" + InpcompMap.get("i" + Integer.toString(plId)).getValue());
        System.out.println("key " + "i" + Integer.toString(plId));
        InpcompMap.get("i" + Integer.toString(plId)).setStyle(PLACE_STYLE_SELECTED);
       // System.out.println(InpcompMap.values());
        graphComponent.refresh();
    }

    @Override
    public void placeDeselect(int plId) {
        System.out.println("Deselect stuff!!");
        InpcompMap.get("i" + Integer.toString(plId)).setStyle(IN_OUT_COMP);
        graphComponent.refresh();
    }

    int extratTransitionId(String id) {
        System.out.println("Out HERE:  " + id);
        int outNr = Integer.parseInt(id.replace("o", ""));
        System.out.println("OutNumber HERE:  " + outNr);
        return outNr;
    }

    int extractPlaceId(String id) {
        System.out.println("String ID:  " + id);
        int plnr = Integer.parseInt(id.replace("i", ""));
        System.out.println("Int ID: " + plnr);
        return plnr;
    }

}
