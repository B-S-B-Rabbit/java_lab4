import java.util.*;
import java.util.concurrent.*;

/**
 * Система управления лифтами.
 * @author Андрей Помошников
 * @version 1.0
 */
public class ElevatorControlSystem {
    /**
     * Очередь вызовов лифтов.
     */
    private static ConcurrentLinkedQueue<Call> calls = new ConcurrentLinkedQueue<>();

    /**
     * Список лифтов.
     */
    private static List<Elevator> elevators = new ArrayList<>();


    /**
     * Конструктор класса ElevatorControlSystem.
     *
     * @param lift1 Первый лифт
     * @param lift2 Второй лифт
     */
    public ElevatorControlSystem(Elevator lift1, Elevator lift2) {
        elevators.add(lift1);
        elevators.add(lift2);
    }

    /**
     * Добавляет вызов лифта.
     *
     * @param call Вызов лифта
     */
    public static synchronized  void addCall(Call call) {
        calls.offer(call);
        assignElevator(call);
    }

    /**
     * Назначает лифт для обработки вызова.
     *
     * @param call Вызов лифта
     */
    private static synchronized  void assignElevator(Call call) {
        Elevator closestElevator = null;
        int closestDistance = Integer.MAX_VALUE;
        for (Elevator elevator : elevators) {
            if (elevator.getDirection() == Direction.STOP) {
                if (closestDistance > elevator.distanceFrom(call.getFloor())) {
                    closestElevator = elevator;
                    closestDistance = elevator.distanceFrom(call.getFloor());
                }
            } else if (elevator.getDirection() == Direction.UP) {
                if (call.getFloor() >= elevator.getCurrentFloor() && call.getDirection() == Direction.UP) {
                    if (closestDistance > elevator.distanceFrom(call.getFloor())) {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceFrom(call.getFloor());
                    }
                } else {
                    if (closestDistance > elevator.distanceDestFromUP(call.getFloor())) {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceFrom(call.getFloor());
                    }
                }
            } else if (elevator.getDirection() == Direction.DOWN) {
                if (call.getFloor() <= elevator.getCurrentFloor() && call.getDirection() == Direction.DOWN) {
                    if (closestDistance > elevator.distanceFrom(call.getFloor())) {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceFrom(call.getFloor());
                    }
                } else {
                    if (closestDistance > elevator.distanceDestFromDown(call.getFloor())) {
                        closestElevator = elevator;
                        closestDistance = elevator.distanceFrom(call.getFloor());
                    }
                }
            }

            if (closestElevator != null) {
                closestElevator.addDestination(call.getFloor());
                if (closestElevator.getDirection() == Direction.STOP) {
                    if (call.getFloor() >= closestElevator.getCurrentFloor()) {
                        closestElevator.setDirection(Direction.UP);
                    } else {
                        closestElevator.setDirection(Direction.DOWN);
                    }
                }
                closestElevator.addToMap(call.getFloor(), call.getDestination());
            }
        }
    }
}
