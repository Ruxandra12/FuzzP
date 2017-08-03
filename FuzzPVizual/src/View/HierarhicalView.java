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
    private static int CHILDREN_SIZE=50;
    private static int INOUT_SIZE=15;
    private static int FONT_SIZE=20;
    
    private static final String IN_OUT_COMP = "shape=ellipse;fillColor=#6C939F;strokeWidth=3;strokeColor=#104050;fontColor=#104050;fontSize="
            + INOUT_SIZE + ";";
    
    private static final String CHILDREN_STYLE = "shape=rectangle;fillColor=#006bff;strokeColor=#104050;fontColor=#104050;fontSize="
            + CHILDREN_SIZE + ";";
    
    private static final String PARENT_STYLE ="shape=rectangle;fillColor=#00fff9;strokeColor=#104050;fontColor=#104050;fontSize="
            + FONT_SIZE + ";";
    
    private static final String EDGE_STYLE = "fillColor=#6C939F;strokeColor=#6C939F;verticalAlign=top;";
   
    private IGlobalController controller;
    private mxGraph graph;
    private mxGraphComponent graphComponent;


    
    private HashMap<IHierarchicalComponent, mxCell> componentMap;
    private HashMap<IHierarchicalComponent.Edge, mxCell> edgeMap;
    private HashMap<String, mxCell> InpcompMap;
    private HashMap<String, mxCell> OutcompMap;
    FuzzyPVizualModel model;

    public HierarhicalView(FuzzyPVizualModel model) {
        this.model = model;
        graph = new mxGraph();       
        componentMap=new HashMap<>();
        edgeMap=new HashMap<>();
        InpcompMap=new HashMap<>();
        OutcompMap=new HashMap<>();
    }

    public void createGraph() {
        Object parent = graph.getDefaultParent();
    
        graph.getModel().beginUpdate();
        model.getHierarchicalModel();
        System.out.println("Creeaza");
        try
        {   
            List<IHierarchicalComponent> mainComp=Arrays.asList(model.getHierarchicalModel());
            drawCell(parent, mainComp, new ArrayList<>());
                        
            
        } finally 
        {
            graph.getModel().endUpdate();
            System.out.println("Finish");
        }
    }

    public mxGraphComponent createGraphComponent() {
        createGraph();
        graphComponent = new mxGraphComponent(graph);
        return graphComponent;
        
    }

    
    public void drawCell(Object parent, List<IHierarchicalComponent> components, List<IHierarchicalComponent.Edge> edges)
    {
        
        for(IHierarchicalComponent comp: components)
        {
     
           mxCell componentCell=(mxCell) graph.insertVertex(parent, null, comp.getCellName(), 0, 0, PARENT_SIZE, PARENT_SIZE, PARENT_STYLE);
         
           componentMap.put(comp, componentCell);
                             
                  for(int i=0;i<comp.getInputs().size();i++)
                  { 
                     
                      mxCell cell=(mxCell) graph.insertVertex(parent, null, comp.getInputs(), 0, 0, CHILDREN_SIZE, CHILDREN_SIZE, IN_OUT_COMP);
                      mxCell edg= (mxCell) graph.insertEdge(parent, null, "", cell, componentCell);
                      InpcompMap.put("", cell);
                  }
                  
                  for(int i=0;i<comp.getOutputs().size();i++)
                  {
                      
                      mxCell cell=(mxCell) graph.insertVertex(parent, null, comp.getOutputs(), 0, 0, CHILDREN_SIZE, CHILDREN_SIZE, IN_OUT_COMP);
                      mxCell edg= (mxCell) graph.insertEdge(parent, null, "", componentCell, cell);
                      OutcompMap.put("", cell);
                  }  
        }
        
    
       for(IHierarchicalComponent.Edge edge:edges)
       {  
           mxCell edg= (mxCell) graph.insertEdge(parent, null, "", componentMap.get(edge.source), componentMap.get(edge.dest));   
           edgeMap.put(edge, edg);
       }
       
       runLayoutOrganizer(parent);
       
       for(IHierarchicalComponent comp: components)
       {
           drawCell(componentMap.get(comp), comp.getChildrenComponents(), comp.getChildEdges());
           
       }
       runLayoutOrganizer(parent);
    }
    
    public void runLayoutOrganizer(Object...objects ) {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(SwingConstants.NORTH);
        layout.setOrientation(SwingConstants.WEST);
        layout.setMoveParent(true);
        layout.setResizeParent(true);
        layout.isResizeParent();
        
        for(Object o : objects){
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
         createGraph();
        graphComponent.refresh();
       
     }

  
}
