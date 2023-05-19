import java.util.*;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Направление движения: ВВЕРХ, ВНИЗ, СТОП.
 */
enum Direction {
    UP, DOWN, STOP
}

/**
 * Представляет лифт.
 * @author Андрей Помошников
 * @version 1.0
 */
public class Elevator {
    /**
     * Идентификатор лифта.
     */
    public int id;

    /**
     * Текущий этаж лифта.
     */
    private int currentFloor;

    /**
     * Максимальный этаж.
     */
    private int maxFloor;

    /**
     * Минимальный этаж.
     */
    private int minFloor;

    /**
     * Текущее направление движения лифта.
     */
    private Direction direction;

    /**
     * Список этажей для перемещения вверх.
     */
    private List<Integer> destinationsUp;

    /**
     * Список этажей для перемещения вниз.
     */
    private List<Integer> destinationsDown;

    /**
     * Соответствие этажей и целевых этажей.
     */
    private List<Map.Entry<Integer, Integer>> floorToDest;

    /**
     * Флаг работы лифта.
     */
    private boolean isRunning;

    /**
     * Флаг открытия дверей лифта.
     */
    private boolean isOpen;


    /**
     * Создает новый экземпляр лифта.
     *
     * @param id            Идентификатор лифта
     * @param currentFloor  Текущий этаж лифта
     * @param maxFloor      Максимальный этаж
     * @param minFloor      Минимальный этаж
     */
    public Elevator(int id, int currentFloor, int maxFloor, int minFloor) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.maxFloor = maxFloor;
        this.minFloor = minFloor;
        this.destinationsUp = new ArrayList<>();
        this.floorToDest = new ArrayList<>();
        this.destinationsDown = new ArrayList<>();
        this.direction = Direction.STOP;
        this.isRunning = true;
    }

    /**
     * Добавляет соответствие этажа и целевого этажа в карту.
     *
     * @param floor Этаж
     * @param dest  Целевой этаж
     */
    public void addToMap(int floor, int dest) {
        floorToDest.add(new AbstractMap.SimpleEntry<>(floor, dest));
    }

    /**
     * Добавляет целевой этаж в список назначений лифта.
     *
     * @param destination Целевой этаж
     */
    public void addDestination(int destination) {
        if (currentFloor > destination) {
            destinationsDown.add(destination);
            destinationsDown = destinationsDown.stream().distinct().collect(Collectors.toList());
        } else {
            destinationsUp.add(destination);
            destinationsUp = destinationsUp.stream().distinct().collect(Collectors.toList());
        }
        destinationsUp.sort(null);
        destinationsDown.sort((a, b) -> b - a);
    }

    /**
     * Добавляет целевой этаж в список назначений для движения вниз.
     *
     * @param destination Целевой этаж
     */
    public void addDestinationDown(int destination) {
        destinationsDown.add(destination);
        destinationsDown = destinationsDown.stream().distinct().collect(Collectors.toList());
        destinationsDown.sort((a, b) -> b - a);
    }

    /**
     * Добавляет целевой этаж в список назначений для движения вверх.
     *
     * @param destination Целевой этаж
     */
    public void addDestinationUp(int destination) {
        destinationsUp.add(destination);
        destinationsUp = destinationsUp.stream().distinct().collect(Collectors.toList());
        destinationsUp.sort(null);
    }

    /**
     * Возвращает текущий этаж лифта.
     *
     * @return Текущий этаж
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * Возвращает текущее направление движения лифта.
     *
     * @return Направление движения
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Устанавливает направление движения лифта.
     *
     * @param d Направление движения
     */
    public void setDirection(Direction d) {
        this.direction = d;
    }

    /**
     * Возвращает расстояние от указанного этажа до текущего этажа лифта.
     *
     * @param floor Этаж
     * @return Расстояние до этажа
     */
    public int distanceFrom(int floor) {
        return Math.abs(currentFloor - floor);
    }

    /**
     * Возвращает расстояние от указанного этажа до последнего целевого этажа вверху.
     *
     * @param floor Этаж
     * @return Расстояние до последнего целевого этажа вверху
     */
    public int distanceDestFromUP(int floor) {
        return Math.abs(floor - destinationsUp.get(destinationsUp.size() - 1));
    }

    /**
     * Возвращает расстояние от указанного этажа до первого целевого этажа внизу.
     *
     * @param floor Этаж
     * @return Расстояние до первого целевого этажа внизу
     */
    public int distanceDestFromDown(int floor) {
        return Math.abs(floor - destinationsDown.get(0));
    }

    /**
     * Останавливает работу лифта.
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Возвращает состояние открытия дверей лифта.
     *
     * @return true, если двери открыты, в противном случае - false
     */
    public boolean getOpenStatus() {
        return isOpen;
    }

    /**
     * Запускает работу лифта.
     */
    public void run() {
        while (isRunning) {
            if (direction == Direction.UP) {
                if (!destinationsUp.isEmpty()) {
                    int nextFloor = destinationsUp.get(0);
                    if (currentFloor < nextFloor) {
                        currentFloor++;
                        direction = Direction.UP;
                    } else {
                        isOpen = true;
                        try {
                            Thread.sleep(3000); // ждем 1 секунду перед перемещением на следующий этаж
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isOpen = false;
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
                } else if (!destinationsDown.isEmpty()) {
                    direction = Direction.DOWN;
                } else {
                    direction = Direction.STOP;
                }
            } else if (direction == Direction.DOWN) {
                if (!destinationsDown.isEmpty()) {
                    int nextFloor = destinationsDown.get(0);
                    if (currentFloor > nextFloor) {
                        currentFloor--;
                        direction = Direction.DOWN;
                    } else {
                        isOpen = true;
                        try {
                            Thread.sleep(3000); // ждем 1 секунду перед перемещением на следующий этаж
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isOpen = false;
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
                } else if (!destinationsUp.isEmpty()) {
                    direction = Direction.UP;
                } else {
                    direction = Direction.STOP;
                }
            }
            try {
                Thread.sleep(1000); // ждем 1 секунду перед перемещением на следующий этаж
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
