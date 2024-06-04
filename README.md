Fantastic Building Simulation  System

Shitai Stanley Zhao 


_______________________________________________________________________________________
1. About/Overview.

This is my GUI  project for the Fantastic Building Simulation System, implemented using the MVC pattern.

The application allows users to simulate and control an elevator system within a building.

_______________________________________________________________________________________
2. List of features.

Graphical user interface (GUI) for the elevator system

Real-time status display for each elevator (position, direction, out of service, scheduled stops)

Real-time status display for each floor (up and down requests)

Step button to advance the elevator system by one step

Start and stop buttons to control the elevator system

Quit button to exit the program

Add request feature to simulate user requests for elevator service

_______________________________________________________________________________________

3. How To Run. 

Run the main class: /src/main/MainGui.java


_______________________________________________________________________________________
4. How to run the jar file

No arguments needed. The application will start with 3 elevators, 10 floors, and a max capacity of 5.

Upon opening the building is out of service.
You need to use the "Start" button to start the elevator system in order to do meaningful things with the system.

Use the "Step" button to advance the elevator system by one step.

Type in the requested floor number in the two text fields following the "Start Floor" and "Stop Floor" labels.
Then use the "Add Request" button to add a request for elevator service.
The request will be added to the floor's request list.

Use the "Stop" button to stop the elevator system and put it out of service.

Use the "Restart" button to restart the elevator system.

Use the "Quit" button (or close the window) to exit the program.

_______________________________________________________________________________________

5. Design/Model Changes. It is important to document what changes that you have made from earlier designs.
Why were those changes required? You can write these changes in terms of version if you wish.

In previous versions, the model throws a lot of exceptions when the state of the building is stopping or out of service,
or when the input of the user is invalid.

To avoid throwing exceptions, i made changes to the building model such that the application will not do anything when
an input is invalid (such as when the floors are not integers, are empty, or are out of bounds).

I also added a "Restart" button to restart the elevator system, which will clear all requests and reset the system.

_______________________________________________________________________________________

6. Assumptions.

We assume the elevator class is working as intended, and we made no changes to the original Elevator class, except for
adding a method to get the status of the elevator. 
