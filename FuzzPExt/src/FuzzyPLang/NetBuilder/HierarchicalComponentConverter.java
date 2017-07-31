package FuzzyPLang.NetBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.xml.internal.bind.CycleRecoverable.Context;

import core.Drawable.HierarchicalComponent;
import structure.IHierarchicalComponent;
import structure.IHierarchicalComponent.Edge;;


public class HierarchicalComponentConverter{
    protected static  Map<String, String> instances;
   // protected Map<String, String> instances1;
   // protected Map<String, AbstactHierachicalIntermediateNet> declarations1;
    protected static Map<String, AbstactHierachicalIntermediateNet> declarations;
    protected static List<NodeRef> edges;
    protected static HiearchicalIntermediateUnifiedNet net;
    
    public HierarchicalComponentConverter()
    {
        this.instances=new HashMap<>();
        this.declarations=new HashMap<>();
        this.edges=new ArrayList<>();
        mockData();
        this.net=new HiearchicalIntermediateUnifiedNet();
        
    }
    
    
    public static void mockData()
    {
       StaticScope sub=new StaticScope();
       net.addPlace(sub, "_P10");
       net.addPlace(sub, "_P0");
       net.addPlace(sub, "_P3");
       net.addPlace(sub, "_P9");
       net.addPlace(sub, "_P1");
       net.addPlace(sub, "_P5");
       net.addPlace(sub, "_P11");
       net.addPlace(sub, "_P7");
       net.addPlace(sub, "P2");
       net.addPlace(sub, "P3");
       
       net.addTransition(sub, "T1");
       net.addTransition(sub, "T3 +");
       net.addTransition(sub, "T0 +");
       net.addTransition(sub, "T1");
       net.addTransition(sub, "T2");
       net.addTransition(sub, "T5");
       net.addTransition(sub, "T6");
       
       net.addInputs(sub, "iP0");
       net.addInputs(sub, "iP1");
       
       net.addOutTransition(sub, "oT0");
       net.addOutTransition(sub, "oT1");
       
      
       
//       NodeRef first=new NodeRef("1");
//       NodeRef second=new NodeRef("2");
//       net.addArc(sub, first, second);
       
       instances.put("cOne", "cOne");
       instances.put("cTwo", "cTwo");
       declarations.put("simple", net);
       
       
    }
    
    public static IHierarchicalComponent createMockData() {
        HierarchicalComponent comp = new HierarchicalComponent("Main");
        HierarchicalComponent splitter=new HierarchicalComponent("Splitter");
        HierarchicalComponent differ=new HierarchicalComponent("Differ");
        comp.addComponent(splitter);
        comp.addComponent(differ);
        
        comp.addInputs("iP0");
        comp.addOutputs("oT0");
        comp.addOutputs("oT1");
        
        splitter.addInputs("iP1");
        splitter.addInputs("iP2");
        
        
        differ.addOutputs("oT3");
        
//        instances.put("Main", comp);
//        instances.put("Splitter", splitter);
//        instances.put("Differ", differ);
//        
                //cum folosesc un obiect de tipul asta in map
       // AbstactHierachicalIntermediateNet net;
//        declarations.put(1,"Main");
//        declarations.put(2,"Splitter");
//        declarations.put(3,"Differ");
        
        
        
               //cum pun edges in list folosit List<NodeRef>
        
       // Edge edge1=new Edge(1,"Edge1",splitter,differ);
        
        //returnez intermediate net sau hiercomp
        return comp;
    }
    
    public static AbstactHierachicalIntermediateNet createMock()
    {
            return null;
           
    }
    
    public HierarchicalComponent getMainComponent()
    {
        IHierarchicalComponent hC=createMockData();
        HierarchicalComponent main=(HierarchicalComponent) Arrays.asList(instances.get("Main"));
        return main;
    }
    
    public void addComponentsInMain(IHierarchicalComponent main)
    {
        IHierarchicalComponent hC=createMockData();
        
        List<IHierarchicalComponent> components=new ArrayList<>();
        components = hC.getChildrenComponents();
        for (IHierarchicalComponent comp: components)
            addComponentsInMain(comp);
    }
    
    public void addEdgeBetweenHierComp(String firstComp, String secondComp)
    {
        IHierarchicalComponent hC=createMockData();
       // if(instances.get(firstComp) instanceof HierarchicalComponent && instances.get(secondComp)  instanceof HierarchicalComponent )
                  
          //  edges.add(new String[] { firstComp, secondComp });
        
       
    }
    
    public void addEdgeBetweenIOCompAndHierComp()
    {
        
    }
    
    
    public static void main(String args[])
    {
       IHierarchicalComponent hC=createMockData();
       System.out.println(hC);
       for (String i:instances.keySet())
           System.out.println(i+" ");
    }

}