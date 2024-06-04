package building;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import scanerzus.Request;

/**
 * The controller component for the building according to the MVC pattern.
 * First the controller initializes, then starts the building.
 * It will listen to the view for requests and update the model accordingly.
 * This controller will take requests from the view and update the model accordingly.
 * It takes in the building model and the view as parameters.
 * The controller will take in instructions to start the building,
 * the requests passed onto it by the view,
 * and stop instruction that will put the building out of service.
 * The controller will also update the view with the current status of the building.
 */
public class BuildingController {
  private Building building;
  private BuildingView view;

  /**
   * This constructor is used to create a new BuildingController object.
   *
   * @param building The building model.
   * @param view     The view component.
   */
  public BuildingController(Building building, BuildingView view) {
    this.building = building;
    this.view = view;

    // Add listeners to view components
    view.addStepListener(new StepListener());
    view.addStartListener(new StartListener());
    view.addStopListener(new StopListener());

    // Start the elevator system
    updateView();
  }

  private void updateView() {
    view.setStatus(building.getStatus());
    view.setDetailedStatus(building.getReport().toString());
  }

  public void startElevatorSystem() {
    building.startElevatorSystem();
  }

  public void stopElevatorSystem() {
    building.stopElevatorSystem();
  }

  public void step() {
    building.step();
  }

  /**
   * This method is used to add a request to the building.
   *
   * @param startFloor The start floor of the request.
   * @param stopFloor  The stop floor of the request.
   */
  public void addRequest(int startFloor, int stopFloor) {
    Request request = new Request(startFloor, stopFloor);
    try {
      building.addRequest(request);
      updateView();
    } catch (IllegalArgumentException | IllegalStateException e) {
      return;
    }

  }

  /**
   * This method is used to restart the elevator system.
   */
  public void restartElevatorSystem() {
    if (building.restartElevatorSystem()) {
      updateView();
    }
  }

  private class StepListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        updateView();
      } catch (IllegalStateException ex) {
        ex.printStackTrace();
      }
    }
  }

  private class StartListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (building.startElevatorSystem()) {
        updateView();
      }
    }
  }

  private class StopListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      building.stopElevatorSystem();
      updateView();
    }
  }
}
