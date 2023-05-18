import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Timer;

public class ElevatorSimulationGUI extends JFrame {
    private ElevatorPanel elevatorPanel1;
    private ElevatorPanel elevatorPanel2;
    private JButton startButton;
    private JSlider intervalSlider;
    private JSlider requestsSlider;
    private JLabel floorLabel1;
    private JLabel floorLabel2;
    private int interval;
    private int numRequests;
    private  Elevator lift1;
    private  Elevator lift2;
    private boolean elevator1Open;
    private boolean elevator2Open;
    private int elevator1Floor;
    private int elevator2Floor;
    private boolean isRun;

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

    private ElevatorPanel createElevatorPanel() {
        ElevatorPanel elevatorPanel = new ElevatorPanel();
        elevatorPanel.setPreferredSize(new Dimension(100, 200));
        elevatorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return elevatorPanel;
    }

    private void startSimulation() {
        new Thread(new ElevatorSimulationRunnable(elevatorPanel1)).start();
        new Thread(new ElevatorSimulationRunnable(elevatorPanel2)).start();
        isRun = true;
        interval = intervalSlider.getValue() * 1000; // Convert to milliseconds
        numRequests = requestsSlider.getValue();
        Random random = new Random();
        int flr = 0;
        int dst = 0;
        Direction dr = Direction.STOP;
        for (int i = 0; i < numRequests; ++i) {
            flr = random.nextInt(15) + 1;
            do {
                dst = random.nextInt(15) + 1;
            } while (dst == flr);
            if (flr > dst) {
                dr = Direction.DOWN;
            } else {
                dr = Direction.UP;
            }
            ElevatorControlSystem.addCall(new Call(flr, dr, dst));
        }
        // TODO: Start the elevator simulation with the specified interval and number of requests


    }

    private class ElevatorSimulationRunnable implements Runnable {
        private ElevatorPanel elevatorPanel;

        public ElevatorSimulationRunnable(ElevatorPanel elevatorPanel) {
            this.elevatorPanel = elevatorPanel;
        }

        @Override
        public void run() {
            while (isRun)
            {
                try {
                    Thread.sleep(1000); // ждем 1 секунду перед перемещением на следующий этаж
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                    elevatorPanel1.setFloor(lift1.getCurrentFloor());
                    elevatorPanel2.setFloor(lift2.getCurrentFloor());

            }
        }
    }


 class ElevatorPanel extends JPanel {
    private boolean open;
    private int floor;

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

        // Draw the elevator car body
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, width, height);

        if (open) {
            // Draw the open doors
            g2d.setColor(Color.ORANGE);
            g2d.fillRect(0, 0, width / 2, height);
            g2d.fillRect(width / 2, 0, width / 2, height);
        } else {
            // Draw the closed doors
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(0, 0, width / 2, height);
            g2d.fillRect(width / 2, 0, width / 2, height);
        }

        // Add additional 3D effects (shadows, highlights) to make the elevator panel look more realistic
        int shadowWidth = width / 10;
        int highlightWidth = width / 15;

        // Draw shadows
        g2d.setColor(new Color(0, 0, 0, 64));
        g2d.fillRect(0, 0, shadowWidth, height);
        g2d.fillRect(width - shadowWidth, 0, shadowWidth, height);

        // Draw highlights
        g2d.setColor(new Color(255, 255, 255, 64));
        g2d.fillRect(shadowWidth, 0, highlightWidth, height);
        g2d.fillRect(width - shadowWidth - highlightWidth, 0, highlightWidth, height);

        // Draw the floor number dynamically
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String floorText = "Floor: " + floor;
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(floorText);
        int textX = (width - textWidth) / 2;
        int textY = fm.getAscent();
        g2d.drawString(floorText, textX, textY);

        g2d.dispose();
    }
}}