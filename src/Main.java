import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        try {
            // Установка стиля Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ElevatorSimulationGUI().setVisible(true);
            }
        });
    }

}

