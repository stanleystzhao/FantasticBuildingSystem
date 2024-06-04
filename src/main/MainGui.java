package main;

import building.Building;
import building.BuildingController;
import building.BuildingView;

/**
 * The Main class will create the GUI and run the elevator system.
 * It creates the view, the controller and the model for the elevator system.
 * We export a runnable JAR file for the GUI version of the elevator system.
 */
public class MainGui {

  /**
   * The main method for the elevator system.
   * This method creates the elevator system and runs it.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    // Create the building model
    Building building = new Building(10, 3, 5); // Example parameters

    // Create the view
    BuildingView view = new BuildingView();

    // Create the controller
    BuildingController controller = new BuildingController(building, view);

    // Set the controller for the view
    view.setController(controller);

  }
}
