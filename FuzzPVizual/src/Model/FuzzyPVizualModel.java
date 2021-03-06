package Model;

import java.awt.event.HierarchyBoundsAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.function.Function;

import FuzzyPLang.FuzzyPLangMain.FuzzyPLang;
import PetriNetToCode.FuzzyNetMakerCodeGenerator;
import PetriNetToCode.UnifiedNetMakerCodeGenerator;
import UnifiedPLang.UnifiedPLang;
import config.IConfigurator;
import core.Drawable.DrawableNetWithExternalNames;
import core.Drawable.HierarchicalComponent;
import core.Drawable.TransitionPlaceNameStore;
import core.FuzzyPetriLogic.PetriNet.FuzzyPetriNet;
import core.FuzzyPetriLogic.PetriNet.PetriNetJsonSaver;
import core.UnifiedPetriLogic.DrawableUnifiedPetriNetWithExternalNames;
import core.UnifiedPetriLogic.UnifiedPetriNet;
import core.common.AbstractPetriNet;
import core.common.generaltable.IGeneralTable;
import core.common.recoder.FullRecordable;
import core.common.recoder.FullRecorder;
import de.erichseifert.gral.data.DataTable;
import main.ScenarioSaverLoader;
import structure.DrawableNet;
import structure.IHierarchicalComponent;
import structure.IHierarchicalComponent.Edge;

public class FuzzyPVizualModel<TTokenType extends FullRecordable<TTokenType>, TTableType extends IGeneralTable, TOuTableType extends TTableType, TPetriNetType extends AbstractPetriNet<TTokenType, TTableType, TOuTableType>> {

    TPetriNetType net;
    FuzzyPetrinetBehaviourModel<TTokenType> behavourModel;
    DrawableNet drawableNet;
    IHierarchicalComponent hierModel;
    private TransitionPlaceNameStore store;

    IConfigurator<TTokenType, TTableType, TOuTableType, TPetriNetType> myConfig;
    Function<TPetriNetType, IConfigurator<TTokenType, TTableType, TOuTableType, TPetriNetType>> configFactory;

    public FuzzyPVizualModel(
            Function<TPetriNetType, IConfigurator<TTokenType, TTableType, TOuTableType, TPetriNetType>> configFactory) {
        this.configFactory = configFactory;
      //  setHierarchicalModel(hierModel);
        hierModel=new HierarchicalComponent("");
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
    public TPetriNetType getNet() {
        return net;
    }

    public IConfigurator<TTokenType, TTableType, TOuTableType, TPetriNetType> getConfigurator() {
        return myConfig;
    }

    public void save(File file) {
        ScenarioSaverLoader<TPetriNetType, TTokenType> saver = new ScenarioSaverLoader<>(myConfig.getPetriClass());
        saver.setPetriNet(net);
        saver.setFullRec(behavourModel.recorder);
        saver.save(file);
    }

    public void load(File selectedFile) {
        ScenarioSaverLoader<TPetriNetType, TTokenType> loader = new ScenarioSaverLoader<>(myConfig.getPetriClass());
        loader.load(selectedFile, myConfig.getStringConverter());
        setNet(loader.getPetriNet());
        setFullRecorder(loader.getFullRec());
        setDrawableNet(myConfig.getDrawableNetFactory().apply(loader.getPetriNet(),
                TransitionPlaceNameStore.createOrdinarNames(loader.getPetriNet())));
        setNameStore(TransitionPlaceNameStore.createOrdinarNames(loader.getPetriNet()));
    }

    public void saveToJava(File loadedFile) {
        String rez = "";
        String path = loadedFile.getParentFile().toString();
        String fileName = "";
        String netName = loadedFile.getName().replaceFirst("[.][^.]+$", "");

        if (net instanceof FuzzyPetriNet) {
            FuzzyNetMakerCodeGenerator gen = new FuzzyNetMakerCodeGenerator((FuzzyPetriNet) net, getSore(), null);
            rez = gen.createMaker(netName);
            fileName = gen.getGeneratedClassName() + ".java";
        } else {

            UnifiedNetMakerCodeGenerator gen = new UnifiedNetMakerCodeGenerator((UnifiedPetriNet) net, netName,
                    getNameStore());
            rez = gen.generateMaker();
            fileName = gen.getClassName() + ".java";
        }
        System.out.println(path + File.separator + fileName);
        File outFile = new File(path, fileName);
        try {
            PrintWriter writer = new PrintWriter(outFile);
            writer.print(rez);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private TransitionPlaceNameStore getSore() {
        if (store == null) {
            store = TransitionPlaceNameStore.createOrdinarNames(net);
        }
        return store;
    }

    public void loadFuzzyPLang(File selectedFile) {
        if (myConfig.getPetriClass().equals(FuzzyPetriNet.class)) {
            FuzzyPLang lang = new FuzzyPLang();
            lang.loadFile(selectedFile);
            setNet((TPetriNetType) lang.getNet());
            setDrawableNet(new DrawableNetWithExternalNames(lang.getNet(), lang.getNameStore()));
            setFullRecorder(new FullRecorder());
            setNameStore(lang.getNameStore());
        } else {
            UnifiedPLang lang = new UnifiedPLang();
            lang.loadFile(selectedFile);
            UnifiedPetriNet rezNet = lang.getRezNet();
            setNet((TPetriNetType) rezNet);
            setDrawableNet(new DrawableUnifiedPetriNetWithExternalNames(rezNet, lang.getNameStrore()));
            setFullRecorder(new FullRecorder<>());
            setNameStore(lang.getNameStrore());
            IHierarchicalComponent comp=lang.getHierarchicalComponent();
            setHierarchicalModel(comp);
        }
    }

    public void setNameStore(TransitionPlaceNameStore nameStore) {
        this.store = nameStore;
    }
    
    
    public void setNet(TPetriNetType newNet) {
        this.net = newNet;
        myConfig = configFactory.apply(newNet);
    }

    public void setDrawableNet(DrawableNet net) {
        this.drawableNet = net;
    }

    public TransitionPlaceNameStore getNameStore() {
        if (store == null) {
            store = TransitionPlaceNameStore.createOrdinarNames(net);
        }
        return store;
    }

    public IHierarchicalComponent getHierarchicalModel() {
        return hierModel;
    }

    public void setHierarchicalModel(IHierarchicalComponent hm) {
        this.hierModel = hm;
    }

    public DrawableNet getDrowableNet() {
        if (drawableNet == null) {
            drawableNet = myConfig.getDrawableNetFactory().apply(net, getNameStore());
        }
        return drawableNet;
    }

    public void setFullRecorder(FullRecorder<TTokenType> recorder) {
        behavourModel = new FuzzyPetrinetBehaviourModel<>(recorder, myConfig.getDoubleConverter());
    }

    public DataTable getDataForPlace(Integer placeId) {
        return behavourModel.getDataForPlace(placeId);
    }

    public double getMaxForPlace(Integer placeId) {
        return behavourModel.getMaxForPlace(placeId);
    }

    public double getMinForPlace(Integer placeId) {
        return behavourModel.getMinForPlace(placeId);
    }

    public int getTickNr() {
        return behavourModel.getTickNr();
    }

    public TTableType getTableForTranition(int trId) {
        return net.getTableForTransition(trId);
    }

    public void savePetriJsonOnly(File selectedFile) {
        PetriNetJsonSaver<TPetriNetType> tt = new PetriNetJsonSaver<>();
        tt.save(net, selectedFile.getAbsolutePath());
    }

}
