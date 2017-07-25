package structure;

import java.util.List;



public interface IHierarchicalComponent {

    String getCellName();
   
    List<Integer> inputComp();
    List<Integer> outputComp();
    List<Edge> getEdges();
    
    List<IHierarchicalComponent> getChildrenComponents();
    
    
    public static class Edge{
        public int id;
        public String name;
        public IHierarchicalComponent dest;
        
        
        public Edge(int id, String name,IHierarchicalComponent dest)
        {
            this.id=id;
            this.name=name;
            this.dest=dest;
        }
        public String getName()
        {
            return name;
        }
    }
}
