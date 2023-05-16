import java.util.*;
import java.util.concurrent.*;

public class Call {
    private int floor;
    private Direction direction;
    private int destination;

    public Call(int floor, Direction direction, int destination) {
        this.floor = floor;
        this.direction = direction;
        this.destination = destination;
    }

    public int getFloor() {
        return floor;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getDestination() {
        return destination;
    }
}
