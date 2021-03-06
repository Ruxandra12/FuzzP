package UnifiedPLang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import FuzzyPLang.NetBuilder.HiearchicalIntermediateUnifiedNet;
import FuzzyPLang.NetBuilder.HierarchicalComponentConverter;
import FuzzyPLang.NetBuilder.UnifiedHierachicalBuilder;
import UnifiedPLang.gen.UnifiedPLangLexer;
import UnifiedPLang.gen.UnifiedPLangParser;
import core.Drawable.HierarchicalComponent;
import core.Drawable.TransitionPlaceNameStore;
import core.UnifiedPetriLogic.ScaleDeducer;
import core.UnifiedPetriLogic.UnifiedPetriNet;
import core.UnifiedPetriLogic.UnifiedPetriNetChecker;
import structure.IHierarchicalComponent;

public class UnifiedPLang {

  private UnifiedPetriNet rezNet;
  private TransitionPlaceNameStore nameStrore;
  private IHierarchicalComponent hierComp;
  private String erros;

  public UnifiedPLang() {
    erros = "";

  }

  public UnifiedPetriNet getRezNet() {
    return rezNet;
  }

  public TransitionPlaceNameStore getNameStrore() {
    return nameStrore;
  }
  
  public IHierarchicalComponent getHierarchicalComponent() {
      return hierComp;
  }

  public String getErros() {
    return erros;
  }

  public void createNet(InputStream is) {
    ANTLRInputStream stream;
    try {
      stream = new ANTLRInputStream(is);
    } catch (IOException e) {
      throw new RuntimeException("Error with file reading ");
    }
    UnifiedPLangLexer lexer = new UnifiedPLangLexer(stream);
    CommonTokenStream cms = new CommonTokenStream(lexer);
    UnifiedPLangParser parser = new UnifiedPLangParser(cms);
    UnifiedPLangVisitor visitor = new UnifiedPLangVisitor();
    visitor.visit(parser.prog());
    HiearchicalIntermediateUnifiedNet intermedate = visitor.getIntermediateNet();
    intermedate.printStuff(1);
    UnifiedHierachicalBuilder bld = new UnifiedHierachicalBuilder(intermedate, true);
    UnifiedPetriNet net = bld.buildPetriNet();
    HierarchicalComponentConverter conv=new HierarchicalComponentConverter(intermedate,bld.getArcs());
    hierComp=conv.convert();
    if (bld.hasErrors()){
      System.err.println("");
      System.err.println(">>>>>>>>>>>>>>>>>>erros during Petri net buiding: <<<<<<<<<<<<<<<<<<<");
      System.err.println(bld.getErrors());
      erros += bld.getErrors();
    } else {
      System.out.println("");
      System.out.println("No error found");
    }
    UnifiedPetriNetChecker checker = new UnifiedPetriNetChecker();
    if (checker.checkPetriNet(net)) {
      System.out.println("No erros found at checking");
    } else {
      System.err.println(">>>>>>>>>>>>>>>>>>erros during checking net buiding: <<<<<<<<<<<<<<<<<<<");
      System.err.println(checker.getErrorMsg());
      erros += checker.getErrorMsg();
    }
    UnifiedPetriNet scaled = ScaleDeducer.deduceScale(net);
    

    List<Integer> uu = ScaleDeducer.unscaledPlaces(scaled);
    if (uu.isEmpty()) {
      System.out.println("All scales deduced");
    } else {
      System.out.println("Unable to deduce scales of " + uu);
      erros += "Unable to deduce scales of " + uu + "\n";
    }

    rezNet = scaled;
    nameStrore = bld.createNameStoreTransitionFullName();

  }

  public void loadFile(String file) {
    try {
      InputStream stream = new FileInputStream(file);
      createNet(stream);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Error with file reading " + file.toString());
    }
  }

  public void loadFile(File file) {
    try {
      InputStream stream = new FileInputStream(file);
      createNet(stream);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Error with file reading " + file.toString());
    }

  }

  public static void main(String[] args) throws FileNotFoundException {

    InputStream stream = new FileInputStream("simple.upn");
    UnifiedPLang plang = new UnifiedPLang(); 
    plang.createNet(stream);

  }

}
