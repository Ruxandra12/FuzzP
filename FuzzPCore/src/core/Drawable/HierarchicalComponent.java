package core.Drawable;

import java.util.ArrayList;
import java.util.List;

import structure.IHierarchicalComponent;

public class HierarchicalComponent implements IHierarchicalComponent {

    private List<IHierarchicalComponent> components;

    private List<Edge> edges;
    private List<String>  input;
    private List<String>  output;
    private String name;
    
    private List<Integer>  inputComp;
    private List<Integer>  outputComp;
    
    public HierarchicalComponent(String name) { 
       
        edges=new ArrayList<>();
        components=new ArrayList<>();
        input=new ArrayList<>();
        output=new ArrayList<>();
       
        this.name=name;
        
        inputComp=new ArrayList<>();
        outputComp=new ArrayList<>();
    }
    
    public void addComponent(IHierarchicalComponent component) {
        components.add(component);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }
    
   
    
    public void addInputs(String input)
    {
        this.input.add(input);
    }
    
    public void addOutputs(String output)
    {
        this.output.add(output);
    }
    

    
    @Override
    public String getCellName() {
        return name;
    }


    @Override
    public List<Edge> getChildEdges() {
        return this.edges;
    }

    @Override
    public List<IHierarchicalComponent> getChildrenComponents() {
        return components;
    }

   
    @Override
      public List<String> getInputs() {
          return input;
          
      }

      @Override
      public List<String> getOutputs() {
          return output;
      }

    @Override
    public List<Integer> inputComp() {
        
        return inputComp;
    }

    @Override
    public List<Integer> outputComp() {
      
        return outputComp;
    }

    public void addInputComp(int i) {
        inputComp.add(i);
        
    }

    public void addOutputComp(int i) {
       outputComp.add(i);
        
    }
      
}
