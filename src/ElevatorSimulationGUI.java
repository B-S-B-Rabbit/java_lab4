import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Графический интерфейс для симуляции работы лифтов.
 * @author Андрей Помошников
 * @version 1.0
 */
public class ElevatorSimulationGUI extends JFrame {
    /**
     * Панель лифта 1.
     */
    private ElevatorPanel elevatorPanel1;

    /**
     * Панель лифта 2.
     */
    private ElevatorPanel elevatorPanel2;

    /**
     * Кнопка "Старт".
     */
    private JButton startButton;

    /**
     * Слайдер для выбора интервала.
     */
    private JSlider intervalSlider;

    /**
     * Слайдер для выбора количества запросов.
     */
    private JSlider requestsSlider;

    /**
     * Метка с текущим этажом для лифта 1.
     */
    private JLabel floorLabel1;

    /**
     * Метка с текущим этажом для лифта 2.
     */
    private JLabel floorLabel2;

    /**
     * Интервал между запросами (в секундах).
     */
    private int interval;

    /**
     * Количество запросов.
     */
    private int numRequests;

    /**
     * Лифт 1.
     */
    private Elevator lift1;

    /**
     * Лифт 2.
     */
    private Elevator lift2;

    /**
     * Флаг, указывающий, запущена ли симуляция.
     */
    private boolean isRun;


    /**
     * Конструктор класса ElevatorSimulationGUI.
     */
    public ElevatorSimulationGUI() {
        setTitle("Elevator Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        elevatorPanel1 = createElevatorPanel();
        elevatorPanel2 = createElevatorPanel();

        JPanel elevatorContainer = new JPanel();
        elevatorContainer.setLayout(new GridLayout(1, 2));
        elevatorContainer.add(elevatorPanel1);
        elevatorContainer.add(elevatorPanel2);

        add(elevatorContainer, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        intervalSlider = new JSlider(1, 10, 1);
        intervalSlider.setMajorTickSpacing(1);
        intervalSlider.setPaintTicks(true);
        intervalSlider.setPaintLabels(true);

        requestsSlider = new JSlider(1, 10, 1);
        requestsSlider.setMajorTickSpacing(1);
        requestsSlider.setPaintTicks(true);
        requestsSlider.setPaintLabels(true);

        floorLabel1 = new JLabel();
        floorLabel2 = new JLabel();

        startButton = new JButton("Start");
        startButton.addActionListener(e -> startSimulation());

        controlPanel.add(new JLabel("Interval:"));
        controlPanel.add(intervalSlider);
        controlPanel.add(new JLabel("Requests:"));
        controlPanel.add(requestsSlider);
        controlPanel.add(startButton);
        controlPanel.add(floorLabel1);
        controlPanel.add(floorLabel2);

        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        lift1 = new Elevator(1, 1, 15, 1);
        lift2 = new Elevator(2, 1, 15, 1);
        ElevatorControlSystem evc = new ElevatorControlSystem(lift1, lift2);
        // создаем потоки для каждого лифта
        Thread thread1 = new Thread(lift1::run);
        Thread thread2 = new Thread(lift2::run);

        // запускаем потоки
        thread1.start();
        thread2.start();
    }

    /**
     * Создает панель для отображения лифта.
     *
     * @return Объект ElevatorPanel
     */
    private ElevatorPanel createElevatorPanel() {
        ElevatorPanel elevatorPanel = new ElevatorPanel();
        elevatorPanel.setPreferredSize(new Dimension(100, 300));
        elevatorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return elevatorPanel;
    }

    /**
     * Запускает симуляцию работы лифтов.
     */
    private void startSimulation() {

        new Thread(new ElevatorSimulationRunnable(elevatorPanel1)).start();
        new Thread(new ElevatorSimulationRunnable(elevatorPanel2)).start();

        isRun = true;
        interval = intervalSlider.getValue() * 1000; // Преобразование в миллисекунды
        numRequests = requestsSlider.getValue();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            private int count = 0;
            private final Random random = new Random();

            @Override
            public void run() {
                int flr = random.nextInt(15) + 1;
                int dst;
                Direction dr;

                do {
                    dst = random.nextInt(15) + 1;
                } while (dst == flr);

                if (flr > dst) {
                    dr = Direction.DOWN;
                } else {
                    dr = Direction.UP;
                }
                try {
                    ElevatorControlSystem.addCall(new Call(flr, dr, dst));
                }
                catch (Exception ignored)
                {}
                count++;
                if (count >= numRequests) {
                    // Остановить таймер после добавления всех вызовов
                    timer.cancel();
                }
            }
        }, 0, interval);
    }

    /**
     * Внутренний класс для выполнения симуляции лифта в отдельном потоке.
     */
    private class ElevatorSimulationRunnable implements Runnable {
        private ElevatorPanel elevatorPanel;

        /**
         * Конструктор класса ElevatorSimulationRunnable.
         *
         * @param elevatorPanel Панель лифта
         */
        public ElevatorSimulationRunnable(ElevatorPanel elevatorPanel) {
            this.elevatorPanel = elevatorPanel;
        }

        @Override
        public void run() {
            while (isRun) {
                try {
                    Thread.sleep(1000); // ждем 1 секунду перед перемещением на следующий этаж
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(elevatorPanel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                elevatorPanel1.setFloor(lift1.getCurrentFloor());
                elevatorPanel2.setFloor(lift2.getCurrentFloor());
                elevatorPanel1.open = lift1.getOpenStatus();
                elevatorPanel2.open = lift2.getOpenStatus();
            }
        }
    }

    /**
     * Панель для отображения состояния лифта.
     */
    static class ElevatorPanel extends JPanel {
        /**
         * Флаг открытия дверей лифта.
         */
        private boolean open;

        /**
         * Этаж, на котором находится лифт. По умолчанию - 1.
         */
        private int floor = 1;


        /**
         * Устанавливает текущий этаж.
         *
         * @param floor Этаж
         */
        public void setFloor(int floor) {
            this.floor = floor;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            int width = getWidth();
            int height = getHeight();

            // Рисуем корпус лифта
            g2d.setColor(Color.GRAY);
            g2d.fillRect(0, 0, width, height);

            if (open) {
                // Рисуем открытые двери
                g2d.setColor(Color.RED);
                g2d.fillRect(0, 0, width / 2, height);
                g2d.fillRect(width / 2, 0, width / 2, height);
            } else {
                // Рисуем закрытые двери
                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRect(0, 0, width / 2, height);
                g2d.fillRect(width / 2, 0, width / 2, height);
            }

            // Добавляем дополнительные 2D-эффекты (тени, подсветки), чтобы панель лифта выглядела более реалистичной
            int shadowWidth = width / 10;
            int highlightWidth = width / 15;

            // Рисуем тени
            g2d.setColor(new Color(0, 0, 0, 64));
            g2d.fillRect(0, 0, shadowWidth, height);
            g2d.fillRect(width - shadowWidth, 0, shadowWidth, height);

            // Рисуем подсветки
            g2d.setColor(new Color(255, 255, 255, 64));
            g2d.fillRect(shadowWidth, 0, highlightWidth, height);
            g2d.fillRect(width - shadowWidth - highlightWidth, 0, highlightWidth, height);

            // Рисуем номер этажа динамически
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Courier New", Font.BOLD, 24));
            String floorText = "Floor: " + floor;
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(floorText);
            int textX = (width - textWidth) / 2;
            int textY = fm.getAscent();
            g2d.drawString(floorText, textX, textY);

            g2d.dispose();
        }
    }
}
