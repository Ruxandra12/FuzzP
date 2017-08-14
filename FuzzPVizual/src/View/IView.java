package View;

import Controller.IGlobalController;

public interface IView {
  default void placeSelected(int plId) {
  }

  default void placeDeselect(int plId) {
  }

  default void transitionSelected(int trId) {
  }

  default void transitionDeselected(int currentlySelectedTransition) {
  }
  
  default void placeSelectedPlot(int placeId) {
  }

  void reset();

  void setController(IGlobalController controller);

  void tranForPlot(int trId);

 

}
