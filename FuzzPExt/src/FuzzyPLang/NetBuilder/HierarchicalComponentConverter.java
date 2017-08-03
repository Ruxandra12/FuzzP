package FuzzyPLang.NetBuilder;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observable.*;

import core.Drawable.HierarchicalComponent;
import core.UnifiedPetriLogic.IUnifiedTable;
import core.UnifiedPetriLogic.UnifiedPetriNet;
import structure.IHierarchicalComponent;
import structure.IHierarchicalComponent.Edge;;


public class HierarchicalComponentConverter{
   
    protected  Map<String, String> instances;
    protected  Map<String, AbstactHierachicalIntermediateNet> declarations;
    protected  List<NodeRef[]> arcs;
    protected  HiearchicalIntermediateUnifiedNet net;
   
    
    public HierarchicalComponentConverter(HiearchicalIntermediateUnifiedNet net, List<NodeRef[]> arcs)
    {
       this.net = net;
       this.arcs = arcs;
    }
    
    public List<String> outCompfromOutTransitions()
    {
        List<String> list=new ArrayList<>();
        
        for(String s:net.getOutTransitions())
        {
            list.add(s);
        }
        return list;
    }
    
    public List<String> inCompfromInputPlaces()
    {
        List<String> list=new ArrayList<>();
       for(String s: net.getInpPlaces())
       {
           list.add(s);
       }
       return list;
    }
    
    public void addSubComponent(HierarchicalComponent main, AbstactHierachicalIntermediateNet<IUnifiedTable, HiearchicalIntermediateUnifiedNet> net, DynamicScope sc)
    {          
        Map<DynamicScope, HierarchicalComponent> compMap =new HashMap<>();
        for(String ff: net.instances.keySet())
        {
            HierarchicalComponent ffHier=new HierarchicalComponent(ff);
            main.addComponent(ffHier);
            DynamicScope clone=sc.cloneSubState();
            clone.addSub(ff);
            addSubComponent(ffHier, net.declarations.get(net.instances.get(ff)), clone);
            compMap.put(clone, ffHier);   
        }
        for(NodeRef[]arc:arcs)
        {
            boolean sourceMyChild = compMap.containsKey(arc[0].subState);
            boolean destMyChild =compMap.containsKey(arc[1].subState);
           
            System.out.println("First Comp: "+arc[0]);
            System.out.println("Second Comp: "+arc[1]);
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
          
           
            if(!(arc[0].subState.equals(arc[1].subState)) && sourceMyChild && destMyChild)
            {
               main.addEdge(new Edge(1, "", compMap.get(arc[0].subState), compMap.get(arc[1].subState)));       
            } 
            else if(sourceMyChild && !destMyChild)
            {
               compMap.get(arc[0].subState).addOutputs(arc[0].getNodeName());
               
            }
            else if(destMyChild && !sourceMyChild)
            {
                compMap.get(arc[1].subState).addInputs(arc[1].getNodeName());;
              
            }
          }      
        }
    
       
    public IHierarchicalComponent convert() {
        
        HierarchicalComponent var=new HierarchicalComponent("");
        
       
        
        for(NodeRef[] arc:arcs)
        {
            if(arc[0].subState.current() && (arc[0].getNodeName().startsWith("i") || arc[0].getNodeName().startsWith("I")))
            {
                 var.addInputs(arc[0].getNodeName()); 
            }
            else if(arc[1].subState.current() && (arc[1].getNodeName().startsWith("o") || arc[1].getNodeName().startsWith("O")))
            {
                var.addOutputs(arc[1].getNodeName());
            }
        }
        addSubComponent(var, net, new DynamicScope());
       
        return var;
    }

}