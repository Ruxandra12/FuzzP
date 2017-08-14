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

                mxCell cell = (mxCell) graph.insertVertex(parent, "i" + Integer.toString(counterIn)+"_"+i, comp.getInputs().get(i), 0,
                        0, CHILDREN_SIZE, CHILDREN_SIZE, IN_OUT_COMP);
                mxCell edg = (mxCell) graph.insertEdge(parent, null, "", cell, componentCell);
                InpcompMap.put("i" + Integer.toString(counterIn), cell);
                counterIn++;

                
                 System.out.println("InputCell id: " + cell.getId()+" Cell value: "+cell.getValue()+" verify! "+comp.getInputs().get(i));
                 
            }

            for (int i = 0; i < comp.getOutputs().size(); i++) {

                mxCell cell = (mxCell) graph.insertVertex(parent, "o" + Integer.toString(counterOut)+"_"+i, comp.getOutputs().get(i),
                        0, 0, CHILDREN_SIZE, CHILDREN_SIZE, IN_OUT_COMP);
                mxCell edg = (mxCell) graph.insertEdge(parent, null, "", componentCell, cell);
                OutcompMap.put("o" + Integer.toString(counterOut), cell);
                counterOut++;

              
                System.out.println("OutputCell id: "+cell.getId()+" Cell value: "+cell.getValue()+" verify! "+comp.getOutputs().get(i));
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
        int trId = extratTransitionId(id); //counter id, used to select the output components
        int trIdPlot = extractPlotId(id);  //indice in list of outputs,  used to get the output cell name displayed in plot view bar
        if (selectioOrDeselect) {
            controller.tranSelectionRequest(trId);
            controller.tranSelectedForPlot(trIdPlot);
        } else {
            controller.tranDeselectionRequest(trId);
            
        }
    }

    private void publishPlaceSelection(String id, boolean selectioOrDeselect) { 
        int plnr = extractPlaceId(id);  //counter id, used to select the input components
        int plPlotId = extractIdForPlot(id); // indice in list of inputs, used to get the input cell name displayed in plot view bar 
        if (selectioOrDeselect) {
            controller.placeSelectionReqiest(plnr);
            controller.placeSelectedForPlot(plPlotId);
        } else {
            controller.placeDeselectionReqiest(plnr);
           
        }
    }

    @Override
    public void transitionSelected(int trId) {
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
//         InpcompMap.get("i" + Integer.toString(plId)).setStyle(PLACE_STYLE_SELECTED);
//         graphComponent.refresh();
    }
       
    @Override
    public void placeDeselect(int plId) {
//        System.out.println("Deselect stuff!!");
//        InpcompMap.get("i" + Integer.toString(plId)).setStyle(IN_OUT_COMP);
//        graphComponent.refresh();
    }

    int extratTransitionId(String id) {
      
        System.out.println("Output String:  " + id);
        String s1 = id.replace("o","");
        int trNr = Integer.parseInt(s1.replaceAll("_[0-9]", ""));
        System.out.println("Output Id: "+trNr);
        return trNr;
    }   
    
    int extractPlotId(String id) {
        int trId= Integer.parseInt(id.replaceFirst("o[0-9]_",""));
        System.out.println("OutputListIndex: "+trId);
        return trId;
    }
    
    int extractIdForPlot(String id)
    {
        
        int plnr = Integer.parseInt(id.replaceFirst("i[0-9]_",""));
        System.out.println("InputListIndex: "+plnr);
        return plnr;
    }

    int extractPlaceId(String id) {   
        System.out.println("String ID:  " + id);
        String s1 = id.replace("i","");
        int plnr = Integer.parseInt(s1.replaceAll("_[0-9]", ""));
        System.out.println("Integer ID: " + plnr);
        return plnr;
    }

    @Override
    public void tranForPlot(int trId) {
    }
    
    @Override
    public void placeSelectedPlot(int placeId) { 
    }
 

}
