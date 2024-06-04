package building;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import org.junit.Before;
import org.junit.Test;
import scanerzus.Request;

/**
 * This class tests the Building class.
 */
public class BuildingTest {

  private Building building1;

  /**
   * Sets up the building for testing.
   */
  @Before
  public void setUp() {
    this.building1 = new Building(12, 5, 3);
  }

  @Test
  public void testBuildingNotNull() {
    assertNotNull(this.building1);
  }

  @Test
  public void testBuildingConstructorThrowFloorException() {
    try {
      new Building(0, 5, 3);
      fail("Should have thrown an exception");
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(),
          "Number of floors, elevators, and capacity must be greater than 0.");
    }
  }

  @Test
  public void testBuildingConstructorThrowElevatorException() {
    try {
      new Building(12, 0, 3);
      fail("Should have thrown an exception");
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(),
          "Number of floors, elevators, and capacity must be greater than 0.");
    }
  }

  @Test
  public void testBuildingConstructorThrowCapacityException() {
    try {
      new Building(12, 5, 0);
      fail("Should have thrown an exception");
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(),
          "Number of floors, elevators, and capacity must be greater than 0.");
    }
  }

  @Test
  public void testGetBuildingStatus() {
    assertEquals(this.building1.getElevatorSystemStatus().getSystemStatus(),
        ElevatorSystemStatus.outOfService);
  }

  @Test
  public void testStartElevatorSystem() {
    assertTrue(this.building1.startElevatorSystem());
    assertEquals(this.building1.getElevatorSystemStatus().getSystemStatus(),
        ElevatorSystemStatus.running);
  }

  @Test
  public void testStartElevatorSystemThrowExceptionWhenAlreadyStopped() {
    this.building1.startElevatorSystem();
    this.building1.stopElevatorSystem();
    try {
      this.building1.startElevatorSystem();
      fail("Should have thrown an exception");
    } catch (IllegalStateException e) {
      assertEquals(e.getMessage(), "Elevator system is stopping.");
    }
  }

  @Test
  public void testStartSystemReturnFalseWhenAlreadyRunning() {
    this.building1.startElevatorSystem();
    assertFalse(this.building1.startElevatorSystem());
  }

  @Test
  public void testStopElevatorSystem() {
    this.building1.startElevatorSystem();
    this.building1.stopElevatorSystem();
    assertEquals(this.building1.getElevatorSystemStatus().getSystemStatus(),
        ElevatorSystemStatus.stopping);
  }

  @Test
  public void testStepNormalNoRequests() {
    this.building1.startElevatorSystem();
    try {
      this.building1.step();
    } catch (IllegalStateException e) {
      fail("Should not have thrown an exception");
    }
  }

  @Test
  public void testStepNormalWithOneUpRequest() {
    this.building1.startElevatorSystem();
    assertTrue(this.building1.addRequest(new Request(1, 5)));
    try {
      this.building1.step();
      assertEquals(this.building1.getElevatorSystemStatus().getSystemStatus(),
          ElevatorSystemStatus.running);
    } catch (IllegalStateException e) {
      fail("Should not have thrown an exception");
    }
  }

  @Test
  public void testStepNormalWithOneDownRequest() {
    this.building1.startElevatorSystem();
    assertTrue(this.building1.addRequest(new Request(5, 1)));
    try {
      this.building1.step();
      assertEquals(this.building1.getElevatorSystemStatus().getSystemStatus(),
          ElevatorSystemStatus.running);
    } catch (IllegalStateException e) {
      fail("Should not have thrown an exception");
    }
  }

  @Test
  public void testAddRequestsPassNullRequest() {
    try {
      this.building1.addRequest(null);
      fail("Should have thrown an exception");
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Request is null.");
    }
  }

  @Test
  public void testAddRequestsThrowsExceptionWhenOutOfService() {
    try {
      this.building1.startElevatorSystem();
      this.building1.stopElevatorSystem();
      this.building1.addRequest(new Request(1, 5));
      fail("Should have thrown an exception");
    } catch (IllegalStateException e) {
      assertEquals(e.getMessage(), "Building is not accepting requests.");
    }
  }

  @Test
  public void testStepThrowsExceptionWhenOutOfService() {
    try {
      this.building1.step();
      fail("Should have thrown an exception");
    } catch (IllegalStateException e) {
      assertEquals(e.getMessage(), "Building is not running");
    }
  }

  @Test
  public void testElevatorAssignment() {
    // Add multiple requests going in different directions
    this.building1.startElevatorSystem();
    this.building1.addRequest(new Request(1, 5));  // Going up
    this.building1.addRequest(new Request(10, 3)); // Going down
    this.building1.addRequest(new Request(7, 1));  // Going down
    this.building1.addRequest(new Request(3, 8));  // Going up

    try {
      // Run the system for a few steps to assign requests to elevators
      for (int i = 0; i < 5; i++) {
        this.building1.step();
      }

      // Check that each elevator is occupied
      for (Elevator elevator : this.building1.getElevators()) {
        assertFalse(elevator.isTakingRequests());
      }
    } catch (IllegalStateException e) {
      fail("Should not have thrown an exception");
    }
  }

  @Test
  public void testElevatorAssignmentWithMoreRequests() {
    // Add multiple requests going in different directions
    this.building1.startElevatorSystem();
    this.building1.addRequest(new Request(1, 5));  // Going up
    this.building1.addRequest(new Request(10, 3)); // Going down
    this.building1.addRequest(new Request(7, 1));  // Going down
    this.building1.addRequest(new Request(3, 8));  // Going up
    this.building1.addRequest(new Request(5, 1)); // Going down
    this.building1.addRequest(new Request(10, 2)); // Going down
    this.building1.addRequest(new Request(12, 4)); // Going down
    this.building1.addRequest(new Request(11, 5)); // Going down
    this.building1.addRequest(new Request(10, 6)); // Going down


    try {
      // Run the system for a few steps to assign requests to elevators
      for (int i = 0; i < 5; i++) {
        this.building1.step();
      }

      // Check that each elevator is occupied
      for (Elevator elevator : this.building1.getElevators()) {
        assertFalse(elevator.isTakingRequests());
      }
    } catch (IllegalStateException e) {
      fail("Should not have thrown an exception");
    }
  }

  @Test
  public void testToString() {
    Building building = new Building(12, 5, 3);
    building.startElevatorSystem();
    assertEquals("Number of floors: 12\n"
            + "Number of elevators: 5\n"
            + "Elevator capacity: 3\n"
            + "Elevator system status: Running\n"
            + "Up requests: []\n"
            + "Down requests: []\n"
            + "Elevator 0: Waiting[Floor 0, Time 5]\n"
            + "Elevator 1: Waiting[Floor 0, Time 5]\n"
            + "Elevator 2: Waiting[Floor 0, Time 5]\n"
            + "Elevator 3: Waiting[Floor 0, Time 5]\n"
            + "Elevator 4: Waiting[Floor 0, Time 5]\n",
        building.toString());
  }

}