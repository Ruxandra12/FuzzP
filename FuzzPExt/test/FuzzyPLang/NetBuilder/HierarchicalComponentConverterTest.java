package FuzzyPLang.NetBuilder;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import core.Drawable.HierarchicalComponent;
import core.Drawable.TransitionPlaceNameStore;
import core.UnifiedPetriLogic.UnifiedPetriNet;
import core.common.recoder.FullRecorder;
import structure.IHierarchicalComponent;

public class HierarchicalComponentConverterTest {
    
    private List<NodeRef[]> arcs;
    private HiearchicalIntermediateUnifiedNet net;

    @Before
    public void setUp() {
        net = mockData();
        UnifiedHierachicalBuilder bld = new UnifiedHierachicalBuilder(net, true);
        bld.buildPetriNet();
        arcs = bld.getArcs();
        
    }
    @Test
    public void myTest() {
        
        HierarchicalComponentConverter myConv= new HierarchicalComponentConverter(net, arcs);
        IHierarchicalComponent rez =  myConv.convert();
       
        assertThat(2, is(rez.getChildrenComponents().size()));
        assertThat(3, is(rez.getChildEdges().size()));
        assertThat(1, is(rez.getInputs().size()));
        assertThat(1, is(rez.getOutputs().size()));
        
    }
    
    public HiearchicalIntermediateUnifiedNet mockData() {
        HiearchicalIntermediateUnifiedNet net =  new HiearchicalIntermediateUnifiedNet();
        net.makeDeclaration(new StaticScope(), "FirstComp");
        StaticScope sc = new StaticScope() ;
        sc.addSub("FirstComp");
        net.addInpPlace(sc.cloneSubState(), "iP1", 0.0);
        net.addOutTransition(sc.cloneSubState(), "oT1");
        NodeRef ip1 = new NodeRef("iP1" , new DynamicScope());
        NodeRef ot1 = new NodeRef("oT1",new DynamicScope());
        net.addArc(sc.cloneSubState(), ip1, ot1);
        
    
        net.addInpPlace(new StaticScope(), "iP0", 0.0);
        net.addPlace(new StaticScope(), "P0");
        net.addTransition(new StaticScope(), "t0");
        net.addOutTransition(new StaticScope(), "oT0");
        
        net.makeInstenciation(new StaticScope(), "fOne", "FirstComp");
        net.makeInstenciation(new StaticScope(), "fTwo", "FirstComp");
        DynamicScope empty = new DynamicScope();
        net.addArc(new StaticScope(), new NodeRef("iP0", empty), new NodeRef("t0", empty));
        DynamicScope dinScopeOne = new DynamicScope();
        dinScopeOne.addSub("fOne");
        DynamicScope dinScopeTwo = new DynamicScope();
        dinScopeTwo.addSub("fTwo");
        
        net.addArc(new StaticScope(), new NodeRef("t0",empty ),new NodeRef("iP1", dinScopeOne));
        net.addArc(new StaticScope(), new NodeRef("oT1", dinScopeOne),new NodeRef("iP1", dinScopeTwo) );
        net.addArc(new StaticScope(), new NodeRef("oT1", dinScopeTwo), new NodeRef("P0",empty ) );
        net.addArc(new StaticScope(),  new NodeRef("P0",empty ), new NodeRef("oT0",empty ) );
        
       return net;
     }

}
