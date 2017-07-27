package config;

import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import structure.IHierarchicalComponent;
import structure.IHierarchicalComponent.Edge;
import Model.FuzzyPVizualModel;
import core.Drawable.HierarchicalComponent;

public class HierarchicalComponentTest {
       
       HierarchicalComponent hierComp;
       List<IHierarchicalComponent> listComp;
       
       @Mock
       private FuzzyPVizualModel model;
      
       @Before
       public void setUp() {
           IHierarchicalComponent mainComp = createMockData();  
           model.getHierarchicalModel();
           hierComp=new HierarchicalComponent(model.getHierarchicalModel().getCellName());
           listComp=new ArrayList<>();
       }
       
       @Test
       public void addComponentTest() {
           assertThat(0,is(listComp.size()));
           IHierarchicalComponent c1=new HierarchicalComponent("");
           hierComp.addComponent(c1);
           assertThat(1,is(listComp.size()));
         }
       
       @Test
       public void addEdgesTest() {
          
         }
       
       @Test
       public void addInputCompTest() {
          
         }
       
       @Test
       public void addOutputCompTest() {
          
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
