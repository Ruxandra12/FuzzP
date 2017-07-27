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

public class HierarhicalView2 implements IView {
    
    private static int PARENT_SIZE = 100;
    private static int CHILDREN_SIZE=60;
    
    private static String TR_NR = "TRNR";
    private static String PL_NR = "PLNR";
    private static String TR_ID_PTTRN = "tr-TRNR";
    private static String PL_ID_PTTRN = "pl-PLNR";

    private static String EDGE_TR_TO_PL_PTTRN = "e-tr-TRNR-pl-PLNR";
    private static String EDGE_PL_TO_TR_PTTRN = "e-pl-PLNR-tr-TRNR";

   
    private IGlobalController controller;
    private mxGraph graph;
    private mxGraphComponent graphComponent;
    public List<IHierarchicalComponent> components;
    public List<IHierarchicalComponent.Edge> edges;
    
    private HashMap<IHierarchicalComponent, mxCell> componentMap;
    private HashMap<IHierarchicalComponent.Edge, mxCell> edgeMap;
    
    FuzzyPVizualModel model;

    public HierarhicalView2(FuzzyPVizualModel model) {
        this.model = model;
        graph = new mxGraph();       
        componentMap=new HashMap<>();
        edgeMap=new HashMap<>();
        components=model.getHierarchicalModel().getChildrenComponents();
        edges=model.getHierarchicalModel().getChildEdges(); 
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
           System.out.println("drawCell "+parent.toString()+" "+comp.getCellName());
           mxCell componentCell=(mxCell) graph.insertVertex(parent, null, comp.getCellName(), 0, 0, PARENT_SIZE, PARENT_SIZE);
           componentMap.put(comp, componentCell);
                             
                  for(int i=0;i<comp.inputComp().size();i++)
                  { 
                      System.out.println("drawInputComp "+comp.inputComp().get(i));
                      mxCell cell=(mxCell) graph.insertVertex(parent, null, comp.inputComp().toString(), 0, 0, CHILDREN_SIZE, CHILDREN_SIZE);
                      mxCell edg= (mxCell) graph.insertEdge(parent, null, "", cell, componentCell);
                  }
                  
                  for(int i=0;i<comp.outputComp().size();i++)
                  {
                      System.out.println("drawOutputComp "+comp.outputComp().get(i));
                      mxCell cell=(mxCell) graph.insertVertex(parent, null, comp.outputComp().toString(), 0, 0, CHILDREN_SIZE, CHILDREN_SIZE);
                      mxCell edg= (mxCell) graph.insertEdge(parent, null, "", componentCell, cell);
                  }  
                 
        }
        
    
       for(IHierarchicalComponent.Edge edge:edges)
       {  
           System.out.println("drawEdge"+" "+edge.source.toString()+" "+edge.dest.toString());
           mxCell edg= (mxCell) graph.insertEdge(parent, null, edge.getName(), componentMap.get(edge.source), componentMap.get(edge.dest));   
           edgeMap.put(edge, edg);
       }
       
       runLayoutOrganizer(parent);
       
       for(IHierarchicalComponent comp: components)
       {
           drawCell(componentMap.get(comp), comp.getChildrenComponents(), comp.getChildEdges());
       }
    }
    
    public void createComponents(Object parent)
    {
        mxCell cell = (mxCell) graph.insertVertex(parent, null, model.getHierarchicalModel().getCellName(), 0, 0, PARENT_SIZE, PARENT_SIZE);
        for(int i=0;i<model.getHierarchicalModel().getChildrenComponents().size();i++) {
            mxCell childrenCell = (mxCell) graph.insertVertex(parent, null, model.getHierarchicalModel().getCellName(), 0, 0, CHILDREN_SIZE, CHILDREN_SIZE);
            List<IHierarchicalComponent> comp = model.getHierarchicalModel().getChildrenComponents();
            componentMap.put(comp.get(i), cell);
          
            }
        
    }
    
    public void connectInputComponents(Object parent)
    {
       for(int i=0;i<model.getHierarchicalModel().inputComp().size();i++)
       {
           mxCell edge=(mxCell) graph.insertEdge(parent, null, "EdgeName", model.getHierarchicalModel().inputComp().get(i),componentMap.get(parent));
       }
    }
    
    public void connectOutputComponents(Object parent)
    {
       for(int i=0;i<model.getHierarchicalModel().outputComp().size();i++)
       {
           mxCell edge=(mxCell) graph.insertEdge(parent, null, "EdgeName", model.getHierarchicalModel().outputComp().get(i), componentMap.get(parent));
       }
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
    
    @Override
    public void setController(IGlobalController controller) {
        this.controller = controller;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        
    }

  
}
