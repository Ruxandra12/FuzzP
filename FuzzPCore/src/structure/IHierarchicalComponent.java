package structure;

import java.util.List;



public interface IHierarchicalComponent {

    String getCellName();
   
    List<Integer> inputComp();
    List<Integer> outputComp();
    List<Edge> getChildEdges();
    
    List<IHierarchicalComponent> getChildrenComponents();
    
    
    public static class Edge{
        public int id;
        public String name;
        public IHierarchicalComponent dest;
        public IHierarchicalComponent source;
        
        
        public Edge(int id, String name,IHierarchicalComponent source, IHierarchicalComponent dest)
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
