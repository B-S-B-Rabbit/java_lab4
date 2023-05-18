import java.util.*;
import java.util.concurrent.*;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;


enum Direction {
    UP, DOWN, STOP
}

public class Elevator {
    public int id;
    private int currentFloor;
    private int maxFloor;
    private int minFloor;
    private Direction direction;
    private List<Integer> destinationsUp;
    private List<Integer> destinationsDown;
    private List<Map.Entry<Integer, Integer>> floorToDest;
    private boolean isRunning;

    public Elevator(int id, int currentFloor, int maxFloor, int minFloor) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.maxFloor = maxFloor;
        this.minFloor = minFloor;
        this.destinationsUp = new ArrayList<>();
        this.floorToDest = new ArrayList<>();
        this.destinationsDown= new ArrayList<>();
        this.direction = Direction.STOP;
        this.isRunning = true;
    }

    public void addToMap(int floor, int dest)
    {
        floorToDest.add(new AbstractMap.SimpleEntry<>(floor, dest));
    }
    public void addDestination(int destination) {
        if (currentFloor > destination)
        {
            destinationsDown.add(destination);
            destinationsDown = destinationsDown.stream().distinct().collect(Collectors.toList());
        }
        else  {
            destinationsUp.add(destination);
                destinationsUp = destinationsUp.stream().distinct().collect(Collectors.toList());
        }
        destinationsUp.sort(null);
        destinationsDown.sort((a, b) -> b - a);
    }
    public void addDestinationDown(int destination) {
        destinationsDown.add(destination);
        destinationsDown = destinationsDown.stream().distinct().collect(Collectors.toList());
        destinationsDown.sort((a, b) -> b - a);
    }
    public void addDestinationUp(int destination) {
        destinationsUp.add(destination);
        destinationsUp = destinationsUp.stream().distinct().collect(Collectors.toList());
        destinationsUp.sort(null);
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }
    public void setDirection(Direction d) {
        this.direction = d;
        System.out.println(d);
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
            if (direction == Direction.UP) {
                if (!destinationsUp.isEmpty()) {
                    System.out.println(destinationsUp);
                    int nextFloor = destinationsUp.get(0);
                    if (currentFloor < nextFloor) {
                        currentFloor++;
                        direction = Direction.UP;
                    } else {
                        System.out.println("lift " + id + " has stopped at " + nextFloor);
                        try {
                            Thread.sleep(3000); // ждем 1 секунду перед перемещением на следующий этаж
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        destinationsUp.remove(0);
                        Iterator<Map.Entry<Integer, Integer>> iterator = floorToDest.iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<Integer, Integer> entry = iterator.next();
                            if (nextFloor == entry.getKey()) {
                                if (currentFloor >= entry.getValue()) {
                                    addDestinationDown(entry.getValue());
                                } else {
                                    addDestinationUp(entry.getValue());
                                }
                                iterator.remove(); // Удаление элемента из ArrayList
                            }
                        }

                    }

                }
                else if (!destinationsDown.isEmpty())
                {
                    direction = Direction.DOWN;
                }
                else {
                    direction = Direction.STOP;
                }
            }
            else if (direction == Direction.DOWN) {
                 if (!destinationsDown.isEmpty()) {
                    int nextFloor = destinationsDown.get(0);
                    if (currentFloor > nextFloor) {
                        currentFloor--;
                        direction = Direction.DOWN;
                    } else {
                        System.out.println("lift " + id + " has stopped at " + nextFloor);
                        try {
                            Thread.sleep(3000); // ждем 1 секунду перед перемещением на следующий этаж
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        destinationsDown.remove(0);
                        Iterator<Map.Entry<Integer, Integer>> iterator = floorToDest.iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<Integer, Integer> entry = iterator.next();
                            if (nextFloor == entry.getKey()) {
                                if (currentFloor >= entry.getValue()) {
                                    addDestinationDown(entry.getValue());
                                } else {
                                    addDestinationUp(entry.getValue());
                                }
                                iterator.remove(); // Удаление элемента из ArrayList
                            }
                        }
                    }
                }
                 else if (!destinationsUp.isEmpty())
                 {
                     direction = Direction.UP;
                 }
                 else {
                     direction = Direction.STOP;
                 }
            }
            try {
                Thread.sleep(1000); // ждем 1 секунду перед перемещением на следующий этаж
                System.out.println("lift-" + id + " has reached " + currentFloor + " floor");
                System.out.println("destdown = " + destinationsDown);
                System.out.println('\n');
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}