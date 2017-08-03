package structure;

import java.util.List;



public interface IHierarchicalComponent {

    List<Integer> inputComp();
    List<Integer> outputComp();
    String getCellName();
    List<Edge> getChildEdges();
    
    List<String>  getInputs();
    List<String>  getOutputs();
    
    List<IHierarchicalComponent> getChildrenComponents();
    
    
    public static class Edge{
        public int id;
        public String name;
        public IHierarchicalComponent dest;
        public IHierarchicalComponent source;
        
        
        public Edge(int id, String name, IHierarchicalComponent source, IHierarchicalComponent dest)
        {
            this.id=id;
            this.name=name;
            this.dest=dest; 
            this.source=source;
        }
        
        
        public String getName()
        {
            return name;
        }
    }
}
