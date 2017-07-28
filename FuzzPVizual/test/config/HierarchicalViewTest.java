package config;



import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.spy;
import static org.junit.Assert.*;

import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.view.mxGraph;

import View.HierarhicalView;
import core.Drawable.HierarchicalComponent;
import structure.IHierarchicalComponent;
import structure.IHierarchicalComponent.Edge;
import Model.FuzzyPVizualModel;


@RunWith(MockitoJUnitRunner.class)
public class HierarchicalViewTest {
    
        @Mock
        private FuzzyPVizualModel model;
       
       
        private HierarhicalView hierView;
        
        List<IHierarchicalComponent> childrenComp;
        List<IHierarchicalComponent.Edge> edges;
        mxGraph graph=new mxGraph();
        
        
        @Before
        public void setUp() throws Exception {
            IHierarchicalComponent mainComp = createMockData(); 
            when(model.getHierarchicalModel()).thenReturn(mainComp);
           
            graph=spy(new mxGraph());
            hierView=new HierarhicalView(model);
        }
        
        @Test
       public void test() {
            assertThat(4, is(model.getHierarchicalModel().getChildrenComponents().size()));
            assertThat(4, is(model.getHierarchicalModel().getChildEdges().size()));
            assertThat(1, is(model.getHierarchicalModel().inputComp().size()));
            assertThat(2, is(model.getHierarchicalModel().outputComp().size()));
            hierView.reset();
        }
        
       @Test
       public void addChildrenComponent() {
           assertThat(4, is(model.getHierarchicalModel().getChildrenComponents().size()));
           IHierarchicalComponent c1=new HierarchicalComponent("");
           model.getHierarchicalModel().getChildrenComponents().add(c1);
           assertThat(5, is(model.getHierarchicalModel().getChildrenComponents().size()));
       }
       
       @Test
       public void addEdgeComponent() {
           assertThat(4, is(model.getHierarchicalModel().getChildEdges().size()));
           Edge edge=new IHierarchicalComponent.Edge(1, "", model.getHierarchicalModel().getChildrenComponents().get(1),
                   model.getHierarchicalModel().getChildrenComponents().get(2));
           model.getHierarchicalModel().getChildEdges().add(edge);
           assertThat(5, is(model.getHierarchicalModel().getChildEdges().size()));
       }
       
       @Test
       public void addInputComp() {
           assertThat(1, is(model.getHierarchicalModel().inputComp().size()));
           int iComp1=11;
           int iComp2=99;
           model.getHierarchicalModel().inputComp().add(iComp1);
           model.getHierarchicalModel().inputComp().add(iComp2);
           assertThat(3, is(model.getHierarchicalModel().inputComp().size()));
       }
        
       @Test
       public void addOutputComp() {
           assertThat(2, is(model.getHierarchicalModel().outputComp().size()));
           int oComp=33;
           model.getHierarchicalModel().outputComp().add(oComp);
           assertThat(3, is(model.getHierarchicalModel().outputComp().size()));
       }
       
               
        public static IHierarchicalComponent createMockData() {
            HierarchicalComponent comp = new HierarchicalComponent("comp");
            comp.addInputComp(0);
            comp.addOutputComp(0);
            comp.addOutputComp(1);
            HierarchicalComponent phiComp=new HierarchicalComponent("phiComp");
            phiComp.addInputComp(1);
            phiComp.addInputComp(2);
            
            HierarchicalComponent lComp=new HierarchicalComponent("lComp");
            HierarchicalComponent mComp=new HierarchicalComponent("mComp");
            
            Edge edge1=new Edge(1,"Edge1",phiComp,lComp);
            Edge edge2=new Edge(2,"Edge2",phiComp, mComp);
            
            
            HierarchicalComponent lastComp = new HierarchicalComponent("lastComp");
            Edge edge3=new Edge(3,"Edge3",lComp,lastComp);
            Edge edge4=new Edge(4,"Edge4",mComp,lastComp);
            mComp.addOutputComp(11);
            lastComp.addOutputComp(10);
            
            HierarchicalComponent mainComp = new HierarchicalComponent("mainComp");
            mainComp.addComponent(mComp);
            mainComp.addComponent(lComp);
            mainComp.addComponent(phiComp);
            mainComp.addComponent(lastComp);
            phiComp.addComponent(comp);
            mainComp.addInputComp(3);
            mainComp.addOutputComp(13);
            mainComp.addOutputComp(14);
            mainComp.addEdge(edge1);
            mainComp.addEdge(edge2);
            mainComp.addEdge(edge3);
            mainComp.addEdge(edge4);
            
            return mainComp;
        }
        
        
}
