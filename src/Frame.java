import physics.Physics;
import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame{
    Frame() {
        setTitle("Circular Motion Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        //setLayout(new BorderLayout(10, 10));

        Physics physics = new Physics(5, 0.4);
        SimPanel simPanel = new SimPanel(physics);
        InfoPanel infoPanel = new InfoPanel(physics);
        Timer timer = new Timer(16, e -> {
            physics.update();
            simPanel.repaint();
            infoPanel.update();
        });
        ControlPanel controlPanel = new ControlPanel(physics, timer, simPanel, infoPanel);

        add(controlPanel, BorderLayout.EAST);
        add(simPanel);
        add(infoPanel, BorderLayout.WEST);

        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }
}
