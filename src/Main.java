import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        // создаем экземпляры классов Лифт и Вызов
        Elevator lift1 = new Elevator(1,1,15,1);
        Elevator lift2 = new Elevator(2,1,15,1);
        ElevatorControlSystem evc = new ElevatorControlSystem(lift1, lift2);
        // создаем потоки для каждого лифта
        Thread thread1 = new Thread(lift1::run);
        Thread thread2 = new Thread(lift2::run);

        // запускаем потоки
        thread1.start();
        thread2.start();

        // передаем вызов в систему управления
        ElevatorControlSystem.addCall(new Call(1, Direction.UP, 10));
        // ждем некоторое время, чтобы лифты могли обработать вызов
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ElevatorControlSystem.addCall(new Call(9, Direction.DOWN, 6));
        // ждем некоторое время, чтобы лифты могли обработать вызов
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ElevatorControlSystem.addCall(new Call(2, Direction.UP, 4));

        ElevatorControlSystem.addCall(new Call(3, Direction.DOWN, 2));
        ElevatorControlSystem.addCall(new Call(4, Direction.DOWN, 2));
        ElevatorControlSystem.addCall(new Call(5, Direction.DOWN, 3));
        ElevatorControlSystem.addCall(new Call(4, Direction.UP, 8));
        ElevatorControlSystem.addCall(new Call(3, Direction.UP, 7));
        ElevatorControlSystem.addCall(new Call(1, Direction.UP, 2));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

