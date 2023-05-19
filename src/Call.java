/**
 * Представляет вызов лифта.
 * @author Андрей Помошников
 * @version 1.0
 */
public class Call {
    /**
     * Этаж, с которого был сделан вызов.
     */
    private int floor;

    /**
     * Направление вызова (вверх или вниз).
     */
    private Direction direction;

    /**
     * Этаж, на который требуется переместиться.
     */
    private int destination;


    /**
     * Создает новый вызов лифта.
     *
     * @param floor      Этаж, с которого был сделан вызов
     * @param direction  Направление вызова (вверх или вниз)
     * @param destination Этаж, на который требуется переместиться
     */
    public Call(int floor, Direction direction, int destination) {
        this.floor = floor;
        this.direction = direction;
        this.destination = destination;
    }

    /**
     * Возвращает этаж, с которого был сделан вызов.
     *
     * @return Этаж вызова
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Возвращает направление вызова.
     *
     * @return Направление вызова
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Возвращает этаж, на который требуется переместиться.
     *
     * @return Целевой этаж вызова
     */
    public int getDestination() {
        return destination;
    }
}
