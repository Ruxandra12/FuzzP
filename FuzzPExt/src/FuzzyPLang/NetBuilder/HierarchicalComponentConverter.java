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
    {   System.out.println(net);
        System.out.println(net.declarations+"   "+net.instances);
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
            System.out.println(sourceMyChild);
            System.out.println(destMyChild);
            if(sourceMyChild && destMyChild)
             {
                main.addEdge(new IHierarchicalComponent.Edge(1, "", compMap.get(arc[0].subState), compMap.get(arc[1].subState)));    
                System.out.println();
             }
            else if(sourceMyChild)
            {
               compMap.get(arc[0].subState);
               main.addOutputs(arc[0].getNodeName());
            }
            else if(destMyChild)
            {
                compMap.get(arc[1].subState);
                main.addInputs(arc[1].getNodeName());
            }
        }
    }
    
    public void findEdgesAndInputComps(HierarchicalComponent main)
    {
       
        int i=1;
        for(NodeRef[] arc:arcs)
        {
            if(arc[0].subState.removeFirstSub().isEmpty() && !(arc[0].nodeName.isEmpty()) 
                    && !(arc[1].subState.removeFirstSub().isEmpty()) && !(arc[1].nodeName.isEmpty())) 
                {
                main.addInputs(arc[1].getNodeName());
                }   
            else if(!(arc[0].subState.removeFirstSub().isEmpty()) && !(arc[0].nodeName.isEmpty()) 
                    && arc[1].subState.removeFirstSub().isEmpty() && !(arc[1].nodeName.isEmpty())) 
                {
                main.addOutputs(arc[0].getNodeName());
                }
            else if(!(arc[0].subState.removeFirstSub().isEmpty()) && !(arc[0].nodeName.isEmpty()) 
                    && !(arc[1].subState.removeFirstSub().isEmpty()) && !(arc[1].nodeName.isEmpty())) 
                {
                //main.addEdge(new Edge(1,"Edge",arc[0],arc[1]));
                }
        }
        
    }
    
    public IHierarchicalComponent convert() {
        
        HierarchicalComponent var=new HierarchicalComponent("");
        
        Map<DynamicScope, HierarchicalComponent> compMap=new HashMap<>();
        for(NodeRef[] arc:arcs)
        {
            if(compMap.containsKey(arc[0].subState)==false && compMap.containsKey(arc[1].subState) == true )
            {
                  var.addInputs(arc[0].getNodeName()); 
            }
            else if(compMap.containsKey(arc[0].subState)==true && compMap.containsKey(arc[1].subState) == false)
            {
                var.addOutputs(arc[1].getNodeName());
            }
        }
        
        /*for(NodeRef[] arc:arcs)
        {
            if(arc[0].subState.removeFirstSub().isEmpty() && !(arc[1].subState.removeFirstSub().isEmpty()))
                var.addInputs(arc[0].getNodeName());
            else if( !(arc[0].subState.removeFirstSub().isEmpty()) && arc[1].subState.removeFirstSub().isEmpty()) {
                var.addOutputs(arc[1].getNodeName());
            }
               
        }*/
        addSubComponent(var, net, new DynamicScope());
       
        return var;
    }

}