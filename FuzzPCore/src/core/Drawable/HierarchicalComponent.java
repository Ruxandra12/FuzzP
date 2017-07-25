package core.Drawable;

import java.util.ArrayList;
import java.util.List;

import structure.IHierarchicalComponent;

public class HierarchicalComponent implements IHierarchicalComponent {

    private List<IHierarchicalComponent> components;

    private List<Edge> edges;
    private List<Integer> inputComps;
    private List<Integer> outputComps;
    private String name;
    
    public HierarchicalComponent(String name) {
       
        edges=new ArrayList<>();
        inputComps=new ArrayList<>();
        outputComps=new ArrayList<>();
        components=new ArrayList<>();
        this.name=name;
    }
    
    public void addComponent(IHierarchicalComponent component) {
        components.add(component);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }
    
    public void addInputComp(int inputComp)
    {
        inputComps.add(inputComp);
    }
    
    public void addOutputComp(int outputComp)
    {
        outputComps.add(outputComp);
    }

    
    @Override
    public String getCellName() {
        return name;
    }


    @Override
    public List<Edge> getEdges() {
        return this.edges;
    }

    @Override
    public List<IHierarchicalComponent> getChildrenComponents() {
        return components;
    }

    @Override
    public List<Integer> inputComp() {
        return inputComps;
    }

    
    @Override
    public List<Integer> outputComp() {
        return outputComps;
    }
    
}
