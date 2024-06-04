package building;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * The view component for the building according to the MVC pattern.
 * The view will display the status of the building and the detailed status of the building.
 * The view will also allow the user to add requests to the building.
 * The view will also allow the user to start and stop the building.
 */
public class BuildingView extends JFrame {
  private BuildingController controller;

  private JPanel statusPanel;
  private JLabel statusLabel;
  private JPanel detailedStatusPanel;
  private JTextArea detailedStatusTextArea;
  private JButton addRequestButton;
  private JButton stepButton;
  private JButton startButton;
  private JButton stopButton;

  public BuildingView() {
    this.controller = controller;
    init();
  }

  private void init() {

    // Initialize status panel
    statusPanel = new JPanel();
    statusLabel = new JLabel("System Status: ");
    statusPanel.add(statusLabel);
    statusPanel.setPreferredSize(new Dimension(500, 50));

    // Initialize detailed status panel
    detailedStatusPanel = new JPanel();
    detailedStatusTextArea = new JTextArea(10, 40);
    detailedStatusPanel.add(new JScrollPane(detailedStatusTextArea));
    detailedStatusPanel.setPreferredSize(new Dimension(500, 300));


    // Create control panel
    JPanel controlPanel = new JPanel(new GridLayout(2, 5));

    // Add request panel
    // add a description for the start floor field
    JLabel startFloorLabel = new JLabel("Start Floor: ");
    controlPanel.add(startFloorLabel);
    JTextField startFloorField = new JTextField();
    controlPanel.add(startFloorField);
    // add a description for the stop floor field
    JLabel stopFloorLabel = new JLabel("Stop Floor: ");
    controlPanel.add(stopFloorLabel);
    JTextField stopFloorField = new JTextField();
    controlPanel.add(stopFloorField);

    addRequestButton = new JButton("Add Request");
    addRequestButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        while (startFloorField.getText().isEmpty() || stopFloorField.getText().isEmpty()) {
          return;
        }
        try {
          Integer.parseInt(startFloorField.getText().trim());
          Integer.parseInt(stopFloorField.getText().trim());
        } catch (NumberFormatException ex) {
          return;
        }
        int startFloor = Integer.parseInt(startFloorField.getText().trim());
        int stopFloor = Integer.parseInt(stopFloorField.getText().trim());
        controller.addRequest(startFloor, stopFloor);
      }
    });
    controlPanel.add(addRequestButton);

    // step button
    stepButton = new JButton("Step");
    stepButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.step();
      }
    });
    controlPanel.add(stepButton);

    // start button
    startButton = new JButton("Start");
    startButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.startElevatorSystem();
      }
    });
    controlPanel.add(startButton);

    // stop button
    stopButton = new JButton("Stop");
    stopButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.stopElevatorSystem();
      }
    });
    controlPanel.add(stopButton);

    // restart button
    JButton restartButton = new JButton("Restart");
    restartButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.restartElevatorSystem();
      }
    });
    controlPanel.add(restartButton);

    // quit button
    JButton quitButton = new JButton("Quit");
    quitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    controlPanel.add(quitButton);

    // Set layout for the main frame
    setLayout(new BorderLayout());
    add(statusPanel, BorderLayout.NORTH);
    add(detailedStatusPanel, BorderLayout.CENTER);
    add(controlPanel, BorderLayout.SOUTH);


    // Set frame properties
    setTitle("Fantastic Building Elevator System");
    setSize(600, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public void setStatus(String status) {
    statusLabel.setText("Status: " + status);
  }

  public void setDetailedStatus(String detailedStatus) {
    detailedStatusTextArea.setText(detailedStatus);
  }

  public void addStepListener(ActionListener listener) {
    stepButton.addActionListener(listener);
  }

  public void addStartListener(ActionListener listener) {
    startButton.addActionListener(listener);
  }

  public void addStopListener(ActionListener listener) {
    stopButton.addActionListener(listener);
  }

  public void setController(BuildingController controller) {
    this.controller = controller;
  }
}
