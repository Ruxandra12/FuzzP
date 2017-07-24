package View;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

import Controller.IGlobalController;
import Model.FuzzyPVizualModel;

public class HierarhicalView2 implements IView {
    private static int BIG_SIZE = 40;
    private static int SMALL_SIZE = BIG_SIZE / 10 + 1;
    private static int FONT_SIZE = BIG_SIZE / 2 - SMALL_SIZE;

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

    private static String TR_NR = "TRNR";
    private static String PL_NR = "PLNR";
    private static String TR_ID_PTTRN = "tr-TRNR";
    private static String PL_ID_PTTRN = "pl-PLNR";

    private static String EDGE_TR_TO_PL_PTTRN = "e-tr-TRNR-pl-PLNR";
    private static String EDGE_PL_TO_TR_PTTRN = "e-pl-PLNR-tr-TRNR";

    ArrayList<mxCell> transitionCells;
    ArrayList<mxCell> palceCells;
    ArrayList<mxCell> edgeCells;

    private IGlobalController controller;
    private mxGraph graph;
    private mxGraphComponent graphComponent;
    FuzzyPVizualModel vizualModel;

    public HierarhicalView2(FuzzyPVizualModel model) {
        this.vizualModel = model;
        graph = new mxGraph();
        model.getDrowableNet().getClass();
        model.getHierarchicalModel().getClass();
        // transitionCells = new ArrayList<>();
        // palceCells = new ArrayList<>();
        // edgeCells = new ArrayList<>();
    }

    public void createGraph() {
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        System.out.println("Creeaza");
        try {

            // Object
            // o1=graph.insertVertex(vizualModel.getHierarchicalModel().getParent(parent),
            // null, vizualModel.getHierarchicalModel().getCell(), 10, 25, 24,15);

        } finally {
            graph.getModel().endUpdate();
            System.out.println("Finish");
        }
    }

    public mxGraphComponent createGraphComponent() {
        createGraph();
        graphComponent = new mxGraphComponent(graph);
        // graphComponent.setDragEnabled(false);
        // graphComponent.setEnabled(false);
        // graphComponent.setWheelScrollingEnabled(true);
        // graphComponent.getGraphHandler().setMoveEnabled(false);
        // graphComponent.setToolTips(true);
        return graphComponent;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setController(IGlobalController controller) {
        this.controller = controller;
    }

    private void createArcOnCanvas(Object parent) {
        vizualModel.getDrowableNet().getArcs().forEach(arc -> {
            if (arc.arcFromPlaceToTransition) {
                String id = EDGE_PL_TO_TR_PTTRN.replace(TR_NR, Integer.toString(arc.transitionId)).replace(PL_NR,
                        Integer.toString(arc.placeId));
                mxCell edge = (mxCell) graph.insertEdge(parent, id, arc.label, palceCells.get(arc.placeId),
                        transitionCells.get(arc.transitionId), EDGE_STYLE);
                edgeCells.add(edge);
            } else {
                String id = EDGE_TR_TO_PL_PTTRN.replace(TR_NR, Integer.toString(arc.transitionId)).replace(PL_NR,
                        Integer.toString(arc.placeId));
                mxCell edge = (mxCell) graph.insertEdge(parent, id, arc.label, transitionCells.get(arc.transitionId),
                        palceCells.get(arc.placeId), EDGE_STYLE);
                edgeCells.add(edge);
            }
        });

    }

    private void createPlacesOnCanvas(Object parent) {
        for (int i = 0; i < 10; i++) {
            mxCell plCell = (mxCell) graph.insertVertex(parent, null, vizualModel.getDrowableNet().getPlaceName(i), 0,
                    0, BIG_SIZE, BIG_SIZE, placeStyle(i));
            graph.getCellGeometry(plCell).setOffset(new mxPoint(new mxPoint(0, 150)));
            palceCells.add(plCell);
        }
    }

    private void createTransitionsOnCanvas(Object parent) {
        for (int i = 0; i < 5; i++) {

            mxCell trCell = (mxCell) graph.insertVertex(parent, null, vizualModel.getDrowableNet().getTransitionName(i),
                    0, 0, SMALL_SIZE, BIG_SIZE, transitionStyle(i));
            graph.getCellGeometry(trCell).setOffset(new mxPoint(0, BIG_SIZE - SMALL_SIZE));
            transitionCells.add(trCell);
        }
    }

    private String placeStyle(int i) {
        return (vizualModel.getDrowableNet().isInputPlace(i)) ? PLACE_STYLE_INP : PLACE_STYLE;
    }

    private String placeStyleSelection(int i) {
        return (vizualModel.getDrowableNet().isInputPlace(i)) ? PLACE_STYLE_SELECTED_INP : PLACE_STYLE_SELECTED;
    }

    private String transitionStyle(int trId) {
        return (vizualModel.getDrowableNet().isOuputTransition(trId)) ? TRANSITION_STYLE_OUT : TRANSITION_STYLE;
    }

    private String transitionStyleSelection(int trId) {
        return (vizualModel.getDrowableNet().isOuputTransition(trId)) ? TRANSITION_STYLE_SELECTED_OUT
                : TRANSITION_STYLE_SELECTED;
    }

}
