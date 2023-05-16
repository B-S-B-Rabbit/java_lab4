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
            if (elevator.getDirection() == Direction.UP)
            {
                if (call.getFloor() >= elevator.getCurrentFloor()) {
                       if (closestDistance > elevator.distanceFrom(call.getFloor()))
                       {
                           closestElevator = elevator;
                           closestDistance = elevator.distanceFrom(call.getFloor());

                       }
            }
                else {
                    if (closestDistance > elevator.distanceDestFromUP(call.getFloor()))
                    {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceDestFromUP(call.getFloor());

                    }
                }
            }
            else {
                if (call.getFloor() <= elevator.getCurrentFloor()) {
                    if (closestDistance > elevator.distanceFrom(call.getFloor()))
                    {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceFrom(call.getFloor());

                    }
                }
                else {
                    if (closestDistance > elevator.distanceDestFromDown(call.getFloor()))
                    {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceDestFromDown(call.getFloor());

                    }
                }
            }
        }
        if (closestElevator != null) {
            closestElevator.addDestination(call.getFloor());
            if (call.getDirection() == Direction.UP)
            {
                closestElevator.addDestinationUp(call.getDestination());
            }
            else {
                closestElevator.addDestinationDown(call.getDestination());
            }
            System.out.println("Call assigned to elevator " + closestElevator.id);
        }
    }
}