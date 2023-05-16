import java.util.*;
import java.util.concurrent.*;
enum Direction {
    UP, DOWN
}

public class Elevator {
    public int id;
    private int currentFloor;
    private int maxFloor;
    private int minFloor;
    private Direction direction;
    private List<Integer> destinationsUp;
    private List<Integer> destinationsDown;
    private boolean isRunning;

    public Elevator(int id, int currentFloor, int maxFloor, int minFloor) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.maxFloor = maxFloor;
        this.minFloor = minFloor;
        this.destinationsUp = new ArrayList<>();
        this.destinationsDown= new ArrayList<>();
        this.direction = Direction.UP;
        this.isRunning = true;
    }

    public void addDestination(int destination) {
        if (currentFloor > destination)
        {
            destinationsDown.add(destination);
        }
        else if (currentFloor < destination) {
            destinationsUp.add(destination);
        }
        destinationsUp.sort(null);
        destinationsDown.sort((a, b) -> b - a);
    }
    public void addDestinationDown(int destination) {
            destinationsDown.add(destination);
            destinationsDown.sort((a, b) -> b - a);
    }
    public void addDestinationUp(int destination) {
        destinationsUp.add(destination);
        destinationsUp.sort(null);
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public int distanceFrom(int floor) {
        return Math.abs(currentFloor - floor);
    }
    public int distanceDestFromUP(int floor){
        return Math.abs(floor - destinationsUp.get(destinationsUp.size() - 1));
    }
    public int distanceDestFromDown(int floor){
        return Math.abs(floor - destinationsDown.get(0));
    }
    public void stop() {
        isRunning = false;
    }

    public void run() {
        while (isRunning) {
                if (!destinationsUp.isEmpty()) {
                    int nextFloor = destinationsUp.get(0);
                    if (currentFloor < nextFloor) {
                        currentFloor++;
                        direction = Direction.UP;
                    } else {
                        destinationsUp.remove(0);
                    }
                }
                else if (!destinationsDown.isEmpty()) {
                    int nextFloor = destinationsDown.get(0);
                    if (currentFloor > nextFloor) {
                        currentFloor--;
                        direction = Direction.DOWN;
                    } else {
                        System.out.println("alert!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        destinationsDown.remove(0);
                    }
                }
            try {
                Thread.sleep(1000); // ждем 1 секунду перед перемещением на следующий этаж
                System.out.println("lift-" + id + "has reached " + currentFloor + " floor");

                System.out.println("destdown = " + destinationsDown);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}