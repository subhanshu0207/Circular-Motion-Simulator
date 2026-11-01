import physics.Physics;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

public class ControlPanel extends JPanel {
    Dimension buttonSize = new Dimension(80, 30);

    Color bgColor = new Color(220, 220, 220);
    Color buttonColor = new Color(46, 14, 2);
    Color buttonFontColor = new Color(240, 240, 240);

    JButton startButton, pauseButton, resetButton, exitButton;
    Physics physics;
    Timer timer;
    SimPanel simPanel;
    SliderPanel sliderPanel;
    CheckBoxPanel checkBoxPanel;
    InfoPanel infoPanel;

    final int LENGTH = 200;
    public JButton createButton(String title) {
        Border border = BorderFactory.createEtchedBorder();

        JButton button = new JButton(title);
        button.setFocusable(false);
        button.setForeground(buttonFontColor);
        button.setBackground(buttonColor);
        button.setPreferredSize(buttonSize);
        button.setBorder(border);
        return button;
    }
    public void addButtons() {
        startButton = createButton("START");
        pauseButton = createButton("PAUSE");
        resetButton = createButton("RESET");
        exitButton = createButton("EXIT");

        startButton.addActionListener(e -> {
            physics.time.resume();
            timer.start();
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
        });

        pauseButton.addActionListener(e -> {
            timer.stop();
            physics.time.pause();
            pauseButton.setEnabled(false);
            startButton.setEnabled(true);
        });

        resetButton.addActionListener(e -> {
            physics.reset();
            physics.time.reset();
            timer.stop();
            sliderPanel.reset();
            checkBoxPanel.reset();
            simPanel.repaint();

            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
        });

        exitButton.addActionListener(e -> {
            timer.stop();
            System.exit(0);
        });

        add(startButton);
        add(pauseButton);
        add(resetButton);
        add(exitButton);
    }
    ControlPanel(Physics physics, Timer timer, SimPanel simPanel, InfoPanel infoPanel) {
        this.physics = physics;
        this.timer = timer;
        this.simPanel = simPanel;
        this.infoPanel = infoPanel;
        sliderPanel = new SliderPanel(physics, simPanel, infoPanel);
        checkBoxPanel = new CheckBoxPanel(simPanel, infoPanel);

        setPreferredSize(new Dimension(LENGTH, 0));
        setBackground(bgColor);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        addButtons();
        add(sliderPanel);
        add(checkBoxPanel);
    }
}