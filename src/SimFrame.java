import physics.Physics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Frame extends JFrame {
    Frame() {
        setTitle("Circular Motion Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);

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
        addTitleBar();

        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }

    private void addTitleBar() {
        setUndecorated(true);

        int topPanelHeight = 30;
        Color topPanelColor = new Color(30, 30, 30);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(0, topPanelHeight));
        topPanel.setBackground(topPanelColor);

        JLabel title = new JLabel("  Circular Motion Simulator");
        title.setFont(new Font("SimSun", Font.BOLD, 15));
        title.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);

        JButton minimize = new JButton("−");
        JButton maximize = new JButton("□");
        JButton close = new JButton("×");

        JButton[] buttons = {minimize, maximize, close};

        for (JButton button : buttons) {
            button.setPreferredSize(new Dimension(45, topPanelHeight));
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.setForeground(Color.WHITE);
            button.setBackground(topPanelColor);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setOpaque(true);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        minimize.addActionListener(e -> setState(JFrame.ICONIFIED));
        minimize.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                minimize.setBackground(topPanelColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                minimize.setBackground(topPanelColor);
            }
        });

        maximize.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        maximize.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                maximize.setBackground(topPanelColor.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                maximize.setBackground(topPanelColor);
            }
        });

        close.addActionListener(e -> System.exit(0));
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                close.setBackground(new Color(200, 50, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                close.setBackground(topPanelColor);
            }
        });

        buttonPanel.add(minimize);
        buttonPanel.add(maximize);
        buttonPanel.add(close);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
    }
}
