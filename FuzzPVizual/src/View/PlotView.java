package View;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.xml.crypto.Data;

import com.sun.javafx.geom.Area;
import com.sun.javafx.geom.Rectangle;
//import com.sun.xml.internal.bind.v2.util.DataSourceSource;

import Controller.IGlobalController;
import Model.FuzzyPVizualModel;
import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.graphics.Location;
import de.erichseifert.gral.plots.DataPoint;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.areas.AreaRenderer;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import structure.IHierarchicalComponent;

public class PlotView extends JFrame implements IView{
  private static final double ellipseRaduis = 2.5;
  public static final Shape pointShape;
  public static final List<Color> pointColors;
  public static final Shape rectangleShape; 
  public static final List<Color> lineColors;
  double insetsTop = 20.0, insetsLeft = 40.0, insetsBottom = 20.0, insetsRight = 100.0;
  static {
    pointShape = new Ellipse2D.Double(-ellipseRaduis, -ellipseRaduis, 2 * ellipseRaduis, 2 * ellipseRaduis);
    rectangleShape = new Rectangle2D.Double(-3.5, -3.5, 7, 7);
    pointColors = new ArrayList<>();
    lineColors = new ArrayList<>();
    pointColors.add(Color.RED);
    pointColors.add(Color.BLUE);
    pointColors.add(Color.GREEN);
    pointColors.add(Color.BLACK);
		pointColors.add(Color.ORANGE);
		pointColors.add(Color.MAGENTA);
		pointColors.add(Color.DARK_GRAY);
    for (int i = 0; i < pointColors.size(); i++) {
      Color ll = pointColors.get(i);
      lineColors.add(new Color(ll.getRed(), ll.getGreen(), ll.getBlue(), 100));
    }

  }

  private InteractivePanel interativePane;
  private FuzzyPVizualModel model;
  private XYPlot plot;
  private boolean placeSelected;
  int cntr;
  HashMap<Integer, DataSeries> placeSeries;
  HashMap<Integer, DataSeries> inputSeries;
  HashMap<Integer, DataSeries> outSeries;
  HashMap<Integer, DataSeries> tranSeries;
  
  @SuppressWarnings("unused")
  private IGlobalController controller;
  private double nowMax;
  private double nowMin;

  JMenuItem viewEvent;
  JMenuItem viewContinous;
  JMenuBar bar;
  JMenu menu;
  
  public PlotView(FuzzyPVizualModel mm) {
    model = mm;
    placeSeries = new HashMap<>();
    inputSeries = new HashMap<>();
    outSeries = new HashMap<>();
    tranSeries = new HashMap<>();
    cntr = 0;
   
    intializeGui();   
    
    bar = new JMenuBar();
    menu = new JMenu("View selected plot");
    viewEvent = new JMenuItem("Event");
    viewContinous = new JMenuItem("Continous");
 
    menu.add(viewEvent);
    menu.add(viewContinous);    
    bar.add(menu);
    this.setJMenuBar(bar);
    this.getContentPane().add(bar);
    this.setJMenuBar(bar);
    this.isVisible();
  }

  public void intializeGui() {
    plot = new XYPlot();
    // adding demo data
    DataTable tt = new DataTable(Double.class, Double.class);
    for (double d = 0.0; d < model.getTickNr(); d++) {
      tt.add(d, Math.sin(d));
    }
    DataSeries dd = new DataSeries("demo data", tt, 0, 1);
    plot.add(dd);
    
    plot.setInsets(new Insets2D.Double(insetsTop, insetsLeft, insetsBottom, insetsRight));
    plot.getAxisRenderer(XYPlot.AXIS_X).setLabel(new Label("time"));
    Label r = new Label("value");
    r.setRotation(90);
    plot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(r);
    interativePane = new InteractivePanel(plot);
    plot.remove(dd); // removing demo data
    plot.setLegendVisible(true);
    plot.setLegendLocation(Location.EAST);
    plot.getLegend().setBorderStroke(null);
    plot.getLegend().setBackground(null);

    plot.getAxis(XYPlot.AXIS_Y).setMax(1.15);
    plot.getAxis(XYPlot.AXIS_Y).setMin(-0.15);
    nowMax = 1.15;
    nowMin = -0.15;
  }
  
  public void showAsEvent(int id)
  {   
      
      System.out.println("Show as Event function");
      
      plot.getAxisRenderer(XYPlot.AXIS_X).setLabel(new Label("time"));
      Label r = new Label("value");
      r.setRotation(90);
      plot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(r);
      
      
      DataSeries ds= addSquareToPlot(model.getNameStore().getPlaceName(id), model.getDataForPlace(id));  
      plot.add(ds);
      interativePane.repaint();
      
  }

  
  public InteractivePanel getInteractivePanel() {
    return interativePane;
  }


  @Override
  public void placeSelected(int plId) {
    
   
    System.out.println("real place id " + plId);
    if (placeSeries.containsKey(plId)) {
      return; // place is already plotted
    }
    
    String name = model.getNameStore().getPlaceName(plId);
    DataTable data = model.getDataForPlace(plId);
   
    // when user selects Continous, plot view will be a continous line
    viewContinous.addActionListener(ll-> { 
    
    DataSeries ss = addToPlotWithName(name, data);
    placeSeries.put(plId, ss);
    updateYAxes(plId); 
    });  
   
   //when user selects Event, plot view will be a rectangle/square
    viewEvent.addActionListener(ll -> showAsEvent(plId));
    
}
  
  @Override
  public void placeSelectedPlot(int plId) { 
      
      //for HierarchicalView - get cell name from input list and display this name in plot view bar, when you select an input cell
      
      String name=model.getHierarchicalModel().getInputs().get(plId);
      DataTable data = model.getDataForPlace(plId);
      DataSeries ss = addToPlotWithName(name, data);
      System.out.println("ID from PLOT: "+plId+" Component from Plot: "+name);
      placeSeries.put(plId, ss);
      
      updateYAxes(plId);   
  }
  
  @Override
  public void tranForPlot(int trId)
  {
      //for HierarchicalView- get cell name from output list and display this name in plot view bar, when you select an output cell
      
      String name=model.getHierarchicalModel().getOutputs().get(trId);
      
      System.out.println("OutputID From Plot: "+trId + " OutComponent from plot: "+name);
      DataTable data = model.getDataForPlace(trId);
      DataSeries ss = addToPlotWithName(name, data);
      tranSeries.put(trId, ss);
      
      updateYAxes(trId); 
  }
  
  private void updateYAxes(Integer placeId) {
    double theoryMax = model.getMaxForPlace(placeId);
    double theoryMin = model.getMinForPlace(placeId);
    double plus = (theoryMax - theoryMin) / 20.0;
    if (nowMax < theoryMax + plus) {
      nowMax = theoryMax + plus;
    }
    if (nowMin > theoryMin - plus) {
      nowMin = theoryMin - plus;
    }
    plot.getAxis(XYPlot.AXIS_Y).setMax(nowMax);
    plot.getAxis(XYPlot.AXIS_Y).setMin(nowMin);
  }

  private DataSeries addToPlotWithName(String name, DataTable data) {
    DataSeries ss = new DataSeries(name, data, 0, 1);
    plot.add(ss);
    LineRenderer ll = new DefaultLineRenderer2D();
    plot.setLineRenderers(ss, ll);
    plot.getPointRenderers(ss).get(0).setColor(getPointColor());
    plot.getPointRenderers(ss).get(0).setShape(pointShape);
    plot.getLineRenderers(ss).get(0).setColor(getLineColor());
        
    interativePane.repaint();
    cntr++;
    return ss;
  }
  
  private DataSeries addSquareToPlot(String name, DataTable data) {
      DataSeries ss = new DataSeries(name, data, 0, 1);
      plot.add(ss);
         
      plot.getPointRenderers(ss).get(0).setShape(rectangleShape);
     
      interativePane.repaint();
      cntr++;
      return ss;
   }

  private Color getPointColor() {
    return pointColors.get(cntr % pointColors.size());
  }

  private Color getLineColor() {
    return lineColors.get(cntr % lineColors.size());
  }

  @Override
  public void placeDeselect(int plId) {
    if (placeSeries.containsKey(plId)) {
      plot.remove(placeSeries.get(plId));
      interativePane.repaint();
      placeSeries.remove(plId);
    }
  }
  
  @Override 
  public void transitionDeselected(int trId)
  {
      if(tranSeries.containsKey(trId))
      {
          plot.remove(tranSeries.get(trId));         
          interativePane.repaint();
          tranSeries.remove(trId);
      }
  }

  @Override
  public void setController(IGlobalController controller) {
    this.controller = controller;
  }

  @Override
  public void reset() {
    for (Integer plId : placeSeries.keySet()) {
      plot.remove(placeSeries.get(plId));
    }
    for (Integer inpID : inputSeries.keySet()) {
      plot.remove(inputSeries.get(inpID));
    }
    for (Integer outId : outSeries.keySet()) {
      plot.remove(outSeries.get(outId));
    }
    for (Integer trId : tranSeries.keySet()) {
      plot.remove(tranSeries.get(trId));  
    }
    placeSeries.clear();
    inputSeries.clear();
    outSeries.clear();
    tranSeries.clear();
    interativePane.repaint();
  }
  
  @Override
  public void transitionSelected(int trId)
  {
  }

}
