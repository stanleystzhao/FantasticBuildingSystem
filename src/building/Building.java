package building;

import building.enums.Direction;
import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import elevator.ElevatorReport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import scanerzus.Request;

/**
 * This class represents a building with elevators.
 * The building has a number of floors, a number of elevators, and a number of people.
 * The building can accept requests for the elevators.
 */
public class Building implements BuildingInterface {


  private final int numberOfFloors;
  private final int numberOfElevators;
  private final int elevatorCapacity;
  private ElevatorSystemStatus status;
  private final List<Elevator> elevators;
  private final List<Request> requests;
  private final List<Request> upRequests;
  private final List<Request> downRequests;


  /**
   * The constructor for the building.
   * This constructor will create a building with the specified number of floors,
   * elevators, and capacity.
   * The building will be initialized with all elevators at the first floor.
   *
   * @param numberOfFloors    the number of floors in the building.
   * @param numberOfElevators the number of elevators in the building.
   * @param elevatorCapacity  the capacity of the elevators in the building.
   */
  public Building(int numberOfFloors, int numberOfElevators, int elevatorCapacity) {
    if (numberOfFloors <= 0 || numberOfElevators <= 0 || elevatorCapacity <= 0) {
      throw new IllegalArgumentException("Number of floors, elevators, and "
          + "capacity must be greater than 0.");
    }
    this.numberOfFloors = numberOfFloors;
    this.numberOfElevators = numberOfElevators;
    this.elevatorCapacity = elevatorCapacity;
    this.status = ElevatorSystemStatus.outOfService;
    this.elevators = new ArrayList<>();
    this.requests = new ArrayList<>();
    this.upRequests = new ArrayList<>();
    this.downRequests = new ArrayList<>();

    for (int i = 0; i < numberOfElevators; i++) {
      elevators.add(new Elevator(numberOfFloors, elevatorCapacity));
    }
  }

  /**
   * This method is used to add a request to the building.
   * @param request the request to add to the building.
   * @return true if the request was added, false otherwise.
   * @throws IllegalStateException if the building is not accepting requests.
   * @throws IllegalArgumentException if the request is null.
   */
  public boolean addRequest(Request request) throws IllegalStateException,
      IllegalArgumentException {
    if (request == null) {
      throw new IllegalArgumentException("Request is null.");
    }
    if (status != ElevatorSystemStatus.running) {
      throw new IllegalStateException("Building is not accepting requests.");
    }

    // filter the input request by keeping floors between 0 and numberOfFloors-1
    if ((request.getStartFloor() < 0) || (request.getStartFloor() >= numberOfFloors)
        || (request.getEndFloor() < 0) || (request.getEndFloor() >= numberOfFloors)) {
      throw new IllegalArgumentException("Invalid floor number.");
    }
    requests.add(request);
    if (request.getDirection() == Direction.UP) {
      upRequests.add(request);
    } else {
      downRequests.add(request);
    }
    return true;
  }

  /**
   * This method is used to distribute requests to the elevators.
   */
  private void distributeRequests() {
    if (requests.isEmpty()) {
      return;
    }
    if (upRequests.isEmpty() && downRequests.isEmpty()) {
      return;
    }
    distributeRequestsForDirection(Direction.DOWN, downRequests);
    distributeRequestsForDirection(Direction.UP, upRequests);
  }

  /**
   * This method is used to distribute requests to the elevators for a specific direction.
   * @param direction the direction of the requests.
   * @param requests the list of requests to distribute.
   */
  private void distributeRequestsForDirection(Direction direction, List<Request> requests) {
    // use iterator to remove requests as they are assigned to elevators
    // because we cannot modify the list while iterating over it
    Iterator<Request> iterator = requests.iterator();
    // while there are still requests to assign
    while (iterator.hasNext()) {
      Request request = iterator.next();
      // find the closest elevator that is moving in the same direction
      Elevator closestElevator = null;
      int closestDistance = Integer.MAX_VALUE;
      for (Elevator elevator : elevators) {
        if (elevator.getDirection() == direction) {
          int distance = Math.abs(elevator.getCurrentFloor() - request.getStartFloor());
          if (distance < closestDistance) {
            closestDistance = distance;
            closestElevator = elevator;
          }
        }
      }
      if (closestElevator != null) {
        closestElevator.addRequest(request);
        iterator.remove();
      }
    }
  }

  /**
   * This method clears the current requests in the building.
   */
  public void clearRequests() {
    requests.clear();
    upRequests.clear();
    downRequests.clear();
  }

  /**
   * This method is used to step the building.
   */
  public void step() throws IllegalStateException {
    if (status == ElevatorSystemStatus.running) {
      // Distribute requests to the elevators
      distributeRequests();
      for (Elevator elevator : elevators) {
        elevator.step();
      }
    } else if (status == ElevatorSystemStatus.outOfService) {
      return;
    } else if (status == ElevatorSystemStatus.stopping) {
      boolean allStopped = true;
      for (Elevator elevator : elevators) {
        if (!elevator.getStatus()) {
          allStopped = false;
          break;
        }
      }
      if (allStopped) {
        status = ElevatorSystemStatus.outOfService;
      }
    }
  }


  /**
   * Starts the elevator system.
   * This method will start all elevators in the building.
   * @return true if the elevator system was started, false otherwise.
   * @throws IllegalStateException if the elevator system is stopping.
   */
  public boolean startElevatorSystem() {
    if (status == ElevatorSystemStatus.stopping) {
      return false;
    }

    if (status == ElevatorSystemStatus.outOfService) {
      for (Elevator elevator : elevators) {
        elevator.start();
      }
      status = ElevatorSystemStatus.running;
      return true;
    }

    return false;
  }

  /**
   * Stops the elevator system.
   * This method will stop all elevators in the building.
   */
  public void stopElevatorSystem() {
    status = ElevatorSystemStatus.stopping;
    for (Elevator elevator : elevators) {
      elevator.takeOutOfService();
    }
    clearRequests();
    this.status = ElevatorSystemStatus.stopping;
  }

  /**
   * This method is used to get the status of the elevator system.
   * @return the status of the elevator system.
   */
  public BuildingReport getElevatorSystemStatus() {
    ElevatorReport[] elevatorReports = new ElevatorReport[elevators.size()];
    for (int i = 0; i < elevators.size(); i++) {
      elevatorReports[i] = elevators.get(i).getElevatorStatus();
    }
    return new BuildingReport(numberOfFloors, numberOfElevators, elevatorCapacity,
        elevatorReports, this.upRequests, this.downRequests, status);
  }

  /**
   * This method is used to get a list of the elevators in the building.
   * @return a list of the elevators in the building.
   */
  public List<Elevator> getElevators() {
    return elevators;
  }

  /** The toString method is used to return a string representation of the Building object.
   * @return a string representation of the Building object.
   */
  public String toString() {
    return this.getElevatorSystemStatus().toString();
  }


  /**
   * This method is used to get the status of the elevator system.
   * (running, stopping, outOfService)
   * @return the status of the elevator system.
   */
  public String getStatus() {
    return status.toString();
  }

  /**
   * This method is used to get the report of the building.
   * @return the report of the building.
   */
  public BuildingReport getReport() {
    return new BuildingReport(numberOfFloors, numberOfElevators, elevatorCapacity,
        elevators.stream().map(Elevator::getElevatorStatus).toArray(ElevatorReport[]::new),
        upRequests, downRequests, status);
  }

  /**
   * This method is used to restart the elevator system.
   * This method only runs when the system is out of service.
   * The program should not crash if the user clicks on restart
   * when the program is running or stopping.
   * @return true if the elevator system was restarted, false otherwise.
   */
  public boolean restartElevatorSystem() {
    if (status == ElevatorSystemStatus.outOfService) {
      for (Elevator elevator : elevators) {
        elevator.start();
      }
      status = ElevatorSystemStatus.running;
      return true;
    }
    return false;
  }
}
