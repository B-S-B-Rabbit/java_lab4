import java.util.*;
import java.util.concurrent.*;

public class ElevatorControlSystem {
        private static ConcurrentLinkedQueue<Call> calls = new ConcurrentLinkedQueue<>();
        private static List<Elevator> elevators = new ArrayList<>();
        private static int maxFloor = 10;
        private static int minFloor = 1;

    public ElevatorControlSystem(Elevator lift1, Elevator lift2){
        elevators.add(lift1);
        elevators.add(lift2);
    }

    public static void addCall(Call call) {
        calls.offer(call);
        System.out.println("Call added: " + call.getFloor() + " " + call.getDirection() + " " + call.getDestination());
        assignElevator(call);
    }

    private static void assignElevator(Call call) {
        Elevator closestElevator = null;
        int closestDistance = Integer.MAX_VALUE;
        for (Elevator elevator : elevators) {
            if (elevator.getDirection() == Direction.STOP) {
                if (closestDistance > elevator.distanceFrom(call.getFloor())) {
                    closestElevator = elevator;
                    closestDistance = elevator.distanceFrom(call.getFloor());
                }
            }
            else if (elevator.getDirection() == Direction.UP) {
                if (call.getFloor() >= elevator.getCurrentFloor() && call.getDirection() == Direction.UP) {
                    if (closestDistance > elevator.distanceFrom(call.getFloor())) {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceFrom(call.getFloor());
                    }
                }
                else if ((call.getFloor() >= elevator.getCurrentFloor() && call.getDirection() == Direction.DOWN))
                {
                    if (closestDistance > elevator.distanceDestFromUP(call.getFloor())) {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceFrom(call.getFloor());
                    }
                }
                else if ((call.getFloor() < elevator.getCurrentFloor() && call.getDirection() == Direction.UP))
                {
                    if (closestDistance > elevator.distanceDestFromUP(call.getFloor())) {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceFrom(call.getFloor());
                    }
                }
                else if ((call.getFloor() < elevator.getCurrentFloor() && call.getDirection() == Direction.DOWN))
                {
                    if (closestDistance > elevator.distanceDestFromUP(call.getFloor())) {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceFrom(call.getFloor());
                    }
                }
            }
            else if (elevator.getDirection() == Direction.DOWN) {
                if (call.getFloor() <= elevator.getCurrentFloor() && call.getDirection() == Direction.DOWN) {
                    if (closestDistance > elevator.distanceFrom(call.getFloor())) {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceFrom(call.getFloor());
                    }
                }
                else if ((call.getFloor() <= elevator.getCurrentFloor() && call.getDirection() == Direction.UP))
                {
                    if (closestDistance > elevator.distanceDestFromDown(call.getFloor())) {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceFrom(call.getFloor());
                    }
                }
                else if ((call.getFloor() > elevator.getCurrentFloor() && call.getDirection() == Direction.UP))
                {
                    if (closestDistance > elevator.distanceDestFromDown(call.getFloor())) {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceFrom(call.getFloor());
                    }
                }
                else if ((call.getFloor() > elevator.getCurrentFloor() && call.getDirection() == Direction.DOWN))
                {
                    if (closestDistance > elevator.distanceDestFromDown(call.getFloor())) {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceFrom(call.getFloor());
                    }
                }
            }
        }

        if (closestElevator != null) {
            closestElevator.addDestination(call.getFloor());
            if (closestElevator.getDirection() == Direction.STOP)
            {
                if (call.getFloor() >= closestElevator.getCurrentFloor())
                {
                    closestElevator.setDirection(Direction.UP);
                }
                else {
                    closestElevator.setDirection(Direction.DOWN);

                }
            }
            closestElevator.addToMap(call.getFloor(), call.getDestination());
            System.out.println("Call assigned to elevator " + closestElevator.id);
        }
    }
}